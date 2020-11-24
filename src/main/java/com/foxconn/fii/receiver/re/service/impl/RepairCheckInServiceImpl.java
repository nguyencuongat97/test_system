package com.foxconn.fii.receiver.re.service.impl;

import com.foxconn.fii.data.b04.repository.B04RepairCheckInRepository;
import com.foxconn.fii.data.b04.repository.B04RepairPTHCheckInRepository;
import com.foxconn.fii.data.b04.repository.B04RepairSMTCheckInRepository;
import com.foxconn.fii.receiver.re.service.RepairCheckInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RepairCheckInServiceImpl implements RepairCheckInService {

    @Autowired
    private B04RepairCheckInRepository b04RepairCheckInRepository;

    @Autowired
    private B04RepairSMTCheckInRepository b04RepairSMTCheckInRepository;

    @Autowired
    private B04RepairPTHCheckInRepository b04RepairPTHCheckInRepository;

    @Override
    public List<Object[]> countByModelNameAndSection(String factory, Date startDate, Date endDate) {
        List<Object[]> result = new ArrayList<>();

        result.addAll(b04RepairCheckInRepository.countByModelNameAndSection(startDate, endDate));
        result.addAll(b04RepairSMTCheckInRepository.countByModelNameAndSection(startDate, endDate));
        result.addAll(b04RepairPTHCheckInRepository.countByModelNameAndSection(startDate, endDate));

        return result;
    }


}
