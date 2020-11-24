package com.foxconn.fii.receiver.re.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/re")
public class ReMvcController {

    @Value("${server.domain}")
    private String domain;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("path", "re-home");
        model.addAttribute("title", "RE Smart Factory Management");
        return "application";
    }

    @RequestMapping("/stock-management")
    public String stockManagement(Model model) {
        model.addAttribute("path", "re-stock-management");
        return "application";
    }

    @RequestMapping("/stock-management/bc8m")
    public String stockBC8MManagement(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("path", "re-stock-bc8m");
        model.addAttribute("title", "RE BC8M Management");
        return "application";
    }

    @RequestMapping("/stock-management/rma")
    public String stockRMAManagement(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("path", "re-stock-rma");
        model.addAttribute("title", "RE RMA Management");
        return "application";
    }

    @RequestMapping("/stock-management/online")
    public String stockOnlineManagement(Model model) {
        model.addAttribute("path", "re-stock-online");
        model.addAttribute("title", "Online");
        return "application";
    }

    @RequestMapping("/online")
    public String stockOnlineWipManagement(Model model) {
        model.addAttribute("path", "re-online-wip");
        model.addAttribute("title", "RE Online WIP Management");
        return "application";
    }

    @RequestMapping("/bonepile")
    public String boneplie(Model model) {
        model.addAttribute("path", "re-bonepile");
        model.addAttribute("title", "RE Bone Pile Management");
        return "application";
    }

    @RequestMapping("/checkin")
    public String checkin(Model model) {
        model.addAttribute("path", "re-checkin");
        model.addAttribute("title", "RE Repair Check In Management");
        return "application";
    }

    @RequestMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("path", "re-checkout");
        model.addAttribute("title", "RE Repair Check Out Management");
        return "application";
    }

    @RequestMapping("/bonepile-detail")
    public String bonepileDetail(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("path", "re-bonepile-detail");
        model.addAttribute("title", "Bone Pile Management");
        return "application";
    }
    // output
    @RequestMapping("/output")
    public String output(Model model) {
        model.addAttribute("path", "re-output");
        return "application";
    }

    @RequestMapping("/capacity")
    public String capacity(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("path", "re-capacity");
        model.addAttribute("title", "RE Repair Engineer Management");
        return "application";
    }

    @RequestMapping("/checklist")
    public String checklist(Model model) {
        model.addAttribute("path", "re-checklist");
        model.addAttribute("title", "RE Checklist");
        return "application";
    }

    @RequestMapping("/checklist/standard")
    public String checklistStandard(Model model) {
        model.addAttribute("path", "re-standard");
        model.addAttribute("title", "RE Checklist Standard");
        return "application";
    }

    @RequestMapping("/checklist/ownerdefine")
    public String checklistOwner(Model model) {
        model.addAttribute("path", "re-ownerdefine");
        model.addAttribute("title", "RE Checklist Ownerdefine");
        return "application";
    }

    @RequestMapping("/checklist/daily")
    public String checklistDaily(Model model) {
        model.addAttribute("path", "re-daily");
        model.addAttribute("title", "RE Checklist Daily");
        return "application";
    }

    @RequestMapping("/checklist/result")
    public String checklistResult(Model model) {
        model.addAttribute("path", "re-result");
        model.addAttribute("title", "RE Checklist Result");
        return "application";
    }


    @RequestMapping("/checklist/leaderconfirm")
    public String checklistLeaderConfirm(Model model) {
        model.addAttribute("path", "re-leaderconfirm");
        model.addAttribute("title", "RE Checklist Leader Confirm");
        return "application";
    }

    @RequestMapping("/checklist/ownerdwfinearea")
    public String checklistOwnerDefine(Model model) {
        model.addAttribute("path", "re-ownerdefinearea");
        model.addAttribute("title", "RE Checklist Owner Define Area");
        return "application";
    }

    @RequestMapping("/checklist/auditor")
    public String auditorCheck(Model model) {
        model.addAttribute("path", "re-auditor");
        model.addAttribute("title", "RE Checklist Auditor");
        return "application";
    }
    @RequestMapping("/machines-stt")
    public String machines(Model model) {
        model.addAttribute("path", "machines-stt");
        return "application";
    }

    @RequestMapping("/line-stt")
    public String aLine(Model model) {
        model.addAttribute("path", "line-stt");
        return "application";
    }

    @RequestMapping("/line-detail")
    public String lines(Model model) {
        model.addAttribute("path", "line-detail");
        return "application";
    }

    @RequestMapping("/in-out-ui")
    public String c03ReCheckInCheckOut(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Task Management");
        model.addAttribute("path", "re-c03-in-out");
        return "application";
    }


    @RequestMapping("/checkinout")
    public String checkinout(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("path", "re-checkin-out");
        model.addAttribute("title", "RE Repair Check In/Out Management");
        return "application";
    }
}
