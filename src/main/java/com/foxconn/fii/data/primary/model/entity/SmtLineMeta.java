package com.foxconn.fii.data.primary.model.entity;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Entity
@Table(name = "smt_line_meta")
public class SmtLineMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "factory")
    private String factory;

    @Column(name = "line_name")
    private String lineName;

    @Column(name = "layout")
    private String layout;

    @Column(name = "mounter_number")
    private Integer mounterNumber;

    @Column(name = "printer_number")
    private Integer printerNumber;

    @Column(name = "printer_time")
    private Float printerTime;

    @Column(name = "ability")
    private Float ability;

    @Column(name = "cft")
    private String cft = "";

    @Transient
    private String modelName = "";

    @Transient
    private String side = "";

    @Transient
    private int totalLine = 1;

    @Transient
    private List<SmtMounterMeta> mounterMetaList;

    @Transient
    private SmtModelMeta modelMeta;

    @Transient
    private SmtModelPrinterMeta modelPrinterMeta;

    @Transient
    private SmtPcasCycleTime modelLineMeta;

    @Transient
    private float printerCycleTime = 0.0f;

    @Transient
    private float pcasCycleTime = 0.0f;

    @Transient
    private float reflowCycleTime = 0.0f;

    @Transient
    private float lineBalance = 0.0f;

    @Transient
    private float realCycleTime = 0.0f;

    @Transient
    private float diff = 0.0f;

    @Transient
    private int linkQty = 0;

    @Transient
    private String status = "OK";

    public void calculateLineBalance() {
        if (mounterNumber == null || printerTime == null || ability == null || layout == null || modelMeta == null || modelPrinterMeta == null || modelLineMeta == null || mounterMetaList == null || mounterMetaList.isEmpty()) {
            return;
        }

        float maxCycleTime = 0.0f;
        float sum = 0.0f;

        int sizeMachineH = 0;
        int sizeMachineG = 0;

//        ArrayList<String> availableMachineH = new ArrayList<>();
//        availableMachineH.add("1");
//        availableMachineH.add("2");
//        availableMachineH.add("3");
//        availableMachineH.add("4");
//        availableMachineH.add("5");
//
//        ArrayList<String> availableMachineG = new ArrayList<>();
//        availableMachineG.add("1");
//        availableMachineG.add("2");
//        availableMachineG.add("3");

        for (SmtMounterMeta mounterMeta : mounterMetaList) {
            if (!StringUtils.isEmpty(mounterMeta.getLayoutSlot())) {
                String slot = mounterMeta.getLayoutSlot().substring(1, 2);
                if (mounterMeta.getLayoutSlot().contains("H")) {
                    sizeMachineH++;
//                    availableMachineH.remove(slot);
                    mounterMeta.setCycleTime(mounterMeta.getCycleTime() + 3f);
                } else {
                    sizeMachineG++;
//                    availableMachineG.remove(slot);
                    mounterMeta.setCycleTime(mounterMeta.getCycleTime() + 4.5f);
                }
            }

            sum += mounterMeta.getCycleTime();
            if (mounterMeta.getCycleTime() > maxCycleTime) {
                maxCycleTime = mounterMeta.getCycleTime();
            }
        }

//        int numberMachineH = Integer.parseInt(layout.substring(0,1));
//        int numberMachineG = Integer.parseInt(layout.substring(2,3));
//
//        for (int i=0; i<numberMachineH - sizeMachineH; i++) {
//            SmtMounterMeta mounterMeta = new SmtMounterMeta();
//            mounterMeta.setLayoutSlot("H" + availableMachineH.get(i));
//            mounterMeta.setCycleTime(maxCycleTime);
//            mounterMetaList.add(mounterMeta);
//        }
//
//        for (int i=0; i<numberMachineG - sizeMachineG; i++) {
//            SmtMounterMeta mounterMeta = new SmtMounterMeta();
//            mounterMeta.setLayoutSlot("G" + availableMachineG.get(i));
//            mounterMeta.setCycleTime(maxCycleTime);
//            mounterMetaList.add(mounterMeta);
//        }

        if (modelPrinterMeta.getPrinterCheckRate() != null && modelPrinterMeta.getPrinterCheckRate() != 0 && modelPrinterMeta.getPrinterCleanRate() != null && modelPrinterMeta.getPrinterCleanRate() != 0) {
            printerCycleTime = printerTime + 30.0f / modelPrinterMeta.getPrinterCheckRate() + 30.0f / modelPrinterMeta.getPrinterCleanRate();
        }

        reflowCycleTime = (modelLineMeta.getReflowSpeed() != null && modelLineMeta.getReflowSpeed() != 0) ? (modelMeta.getPanelLength() + 5) / modelLineMeta.getReflowSpeed() * 60 : 0;

        if (printerTime > maxCycleTime || reflowCycleTime > maxCycleTime) {
            if (printerTime > reflowCycleTime) {
                lineBalance = (sum + printerTime + reflowCycleTime) / printerTime / (mounterMetaList.size() + 2) * 100.0f;
                realCycleTime = modelMeta.getLinkQty() != 0 ? printerTime / modelMeta.getLinkQty() : 0;
            } else {
                lineBalance = (sum + printerTime + reflowCycleTime) / reflowCycleTime / (mounterMetaList.size() + 2) * 100.0f;
                realCycleTime = modelMeta.getLinkQty() != 0 ? reflowCycleTime / modelMeta.getLinkQty() : 0;
            }
        } else {
            lineBalance = mounterMetaList.size() != 0 ? sum / maxCycleTime / mounterMetaList.size() * 100.0f : 0;
            realCycleTime = modelMeta.getLinkQty() != 0 ? maxCycleTime / modelMeta.getLinkQty() : 0;
        }

        diff = pcasCycleTime != 0 ? Math.abs(pcasCycleTime - realCycleTime) * 100 / pcasCycleTime : 0;

        if (lineBalance > 94 && diff > 4) {
            status = "IE need to improve";
        } else if (lineBalance > 94 && diff < 5) {
            status = "OK";
        } else if (lineBalance < 95 && diff < 5) {
            status = "ME program need to improve";
        } else {
            status = "ME Equipment need to improve";
        }
    }

}
