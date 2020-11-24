package com.foxconn.fii.receiver.smt.service.impl;

import com.foxconn.fii.data.primary.model.entity.SmtModelMeta;
import com.foxconn.fii.data.primary.model.entity.SmtPcasCycleTime;
import com.foxconn.fii.data.primary.repository.SmtModelMetaRepository;
import com.foxconn.fii.data.primary.repository.SmtModelOnlineRepository;
import com.foxconn.fii.receiver.smt.service.PcasDataService;
import com.foxconn.fii.receiver.smt.service.SmtModelOnlineService;
import com.foxconn.fii.receiver.smt.service.SmtPcasCycleTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PcasDataServiceImpl implements PcasDataService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SmtModelMetaRepository smtModelMetaRepository;

    @Autowired
    private SmtPcasCycleTimeService smtPcasCycleTimeService;

    @Autowired
    private SmtModelOnlineService smtModelOnlineService;


    @Autowired
    private SmtModelOnlineRepository smtModelOnlineRepository;


    @Override
    public void syncDataPcas() {
        syncPcasB06();
//        syncPcasB04();
    }

    private void syncPcasB06() {
        String factory = "B06";
        List<SmtPcasCycleTime> pcasCycleTimeList = new ArrayList<>();

        List<SmtModelMeta> modelMetaList = smtModelMetaRepository.findByFactory(factory);

        for (SmtModelMeta modelMeta : modelMetaList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("model_name", modelMeta.getModelName());
            map.add("line_name", "");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            ResponseEntity<String> responseEntity;
            try {
                responseEntity = restTemplate.exchange("http://10.224.69.100/Data_ATE_A02/Servicepostdata.asmx/GET_PCAS", HttpMethod.POST, entity, String.class);
            } catch (RestClientException e) {
                log.error("### syncDataPcas error ", e);
                return;
            }

            parseCycleTimeFromXmlB06(responseEntity.getBody(), pcasCycleTimeList, modelMeta, factory);

        }

        smtPcasCycleTimeService.saveAll(pcasCycleTimeList);
        smtModelMetaRepository.saveAll(modelMetaList);
    }

    private void syncPcasB04() {
        String factory = "B04";
        List<SmtPcasCycleTime> pcasCycleTimeList = new ArrayList<>();

        List<SmtModelMeta> modelMetaList = smtModelMetaRepository.findByFactory(factory);

        for (SmtModelMeta modelMeta : modelMetaList) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("model_name", modelMeta.getModelName());
            map.add("line_name", "");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            ResponseEntity<String> responseEntity;
            try {
                responseEntity = restTemplate.exchange("http://10.224.69.100/Data_ATE_A02/Servicepostdata.asmx/GET_PCAS_B04", HttpMethod.POST, entity, String.class);
            } catch (RestClientException e) {
                log.error("### syncDataPcas error ", e);
                return;
            }

            parseCycleTimeFromXmlB06(responseEntity.getBody(), pcasCycleTimeList, modelMeta, factory);

        }

        smtPcasCycleTimeService.saveAll(pcasCycleTimeList);
        smtModelMetaRepository.saveAll(modelMetaList);
    }

    private void parseCycleTimeFromXmlB06(String xml, List<SmtPcasCycleTime> pcasCycleTimeList, SmtModelMeta modelMeta, String factoryName) {
        if (StringUtils.isEmpty(xml)) {
            return;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nList = document.getElementsByTagName("Mytable");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    String modelName = eElement.getElementsByTagName("P_NO").item(0).getTextContent();
                    String lineName = eElement.getElementsByTagName("SFIS_LINE").item(0).getTextContent();
                    String pcasLineName = eElement.getElementsByTagName("PCAS_LINE").item(0).getTextContent();

                    SmtPcasCycleTime pcasCycleTime = new SmtPcasCycleTime();

                    pcasCycleTime.setFactory(factoryName);
                    pcasCycleTime.setSectionName("SMT");

                    pcasCycleTime.setLineName(lineName);
                    pcasCycleTime.setModelName(modelName);
                    pcasCycleTime.setPcasLineName(pcasLineName);
                    pcasCycleTime.setSide(eElement.getElementsByTagName("SIDE").item(0).getTextContent());
                    pcasCycleTime.setPlantName(eElement.getElementsByTagName("PLANT_NAME").item(0).getTextContent());
                    pcasCycleTime.setCycleTime(Float.parseFloat(eElement.getElementsByTagName("CIRCLE_TIME").item(0).getTextContent()));

                    pcasCycleTimeList.add(pcasCycleTime);

                    modelMeta.setLinkQty(Integer.parseInt(eElement.getElementsByTagName("LINK_QTY").item(0).getTextContent()));
                }
            }

        } catch (Exception e) {
            log.error("### parseCycleTimeFromXmlB06 error", e);
        }
    }
}
