package com.foxconn.fii.receiver.pc.controller;

import com.foxconn.fii.common.response.CommonResponse;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.response.ResponseCode;
import com.foxconn.fii.data.PcEcnAgileData;
import com.foxconn.fii.data.primary.model.entity.PcEcn;
import com.foxconn.fii.receiver.pc.service.EcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class EcController {

    @Autowired
    private EcService ecService;

    @PostMapping("/ecn/upload")
    public CommonResponse<Boolean> uploadEcnFromAgile(List<PcEcnAgileData> ecnAgileDataList) {
        List<PcEcn> ecnList = new ArrayList<>();

        if (ecnAgileDataList == null || ecnAgileDataList.isEmpty()) {
            PcEcn ecn = new PcEcn();
            ecn.setFactory("C03");
            ecn.setEcnNumber("ECNUT0000001");
            ecn.setOldMaterial("511.00050.005");
            ecn.setNewMaterial("511.00050.005'");
            ecn.setModelList("F01L040.10 U10C150.00");
            ecnList.add(ecn);

            ecn = new PcEcn();
            ecn.setFactory("C03");
            ecn.setEcnNumber("ECNUT0000002");
            ecn.setOldMaterial("134.00928.005");
            ecn.setNewMaterial("134.00928.005'");
            ecn.setModelList("U10C150.00");
            ecnList.add(ecn);

            ecn = new PcEcn();
            ecn.setFactory("C03");
            ecn.setEcnNumber("ECNUT0000003");
            ecn.setOldMaterial("053.00362.005AB");
            ecn.setNewMaterial("053.00362.005AB'");
            ecn.setModelList("F01L040.10");
            ecnList.add(ecn);
        } else {
            try {
                for (PcEcnAgileData ecnAgileData : ecnAgileDataList) {
                    List<String> oldMaterials = new ArrayList<>();
                    List<String> newMaterials = new ArrayList<>();

                    String[] descLines = ecnAgileData.getEcn().split("\n");
                    for (String descLine : descLines) {
                        String modelList = "";
                        String bom = "";
                        String ecnDate = "";

                        if (descLine.startsWith("MODEL: ")) {
                            modelList = descLine.replace("MODEL: ", "").trim();
                        } else if (descLine.startsWith("BOM:")) {
                            bom = descLine.replace("BOM: ", "").trim();
                        } else if (descLine.startsWith("change PN:")) {
                            String changePN = descLine.replace("change PN:", "").trim();
                            String[] changePNParts = changePN.split(";");
                            if (changePNParts.length == 3) {
                                if (changePNParts[0].contains(" to ")) {
                                    String[] materials = changePNParts[0].split(" to ");
                                    if (materials.length == 2) {
                                        oldMaterials.add(materials[0]);
                                        newMaterials.add(materials[1]);
                                    }
                                }
                            }
                        } else if (descLine.startsWith("DATE: ")) {
                            ecnDate = descLine.replace("DATE: ", "").trim();
                        }

                        for (int i = 0; i < oldMaterials.size(); i++) {
                            PcEcn ecn = new PcEcn();
                            ecn.setFactory(ecnAgileData.getFactory());
                            ecn.setEcnNumber(ecnAgileData.getEcn());
                            ecn.setModelList(modelList);
                            ecn.setOldMaterial(oldMaterials.get(i));
                            ecn.setNewMaterial(newMaterials.get(i));
                            ecn.setEcnDate(ecnDate);

                            if (!StringUtils.isEmpty(ecn.getOldMaterial()) &&
                                    !StringUtils.isEmpty(ecn.getNewMaterial()) &&
                                    !StringUtils.isEmpty(ecn.getModelList()))
                                ecnList.add(ecn);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("uploadEcnFromAgile error", e);
            }
        }

        ecService.evaluatedEffectiveTime(ecnList);
        return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "success", true);
    }

    @GetMapping("/ecn")
    public ListResponse<PcEcn> getEcnList(String factory) {
        return ListResponse.success(ecService.getEcnList(factory));
    }
}