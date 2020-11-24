package com.foxconn.fii.receiver.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.data.IcivetNewsMessage;
import com.foxconn.fii.data.IcivetTextMessage;
import com.foxconn.fii.data.MailMessage;
import com.foxconn.fii.data.NotifyMessage;
import com.foxconn.fii.data.primary.model.entity.TestResourceGroup;
import com.foxconn.fii.data.primary.model.entity.TestTracking;
import com.foxconn.fii.data.primary.repository.TestResourceGroupModelRepository;
import com.foxconn.fii.data.primary.repository.TestResourceGroupRepository;
import com.foxconn.fii.receiver.test.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private TestResourceGroupRepository testResourceGroupRepository;

    @Autowired
    private TestResourceGroupModelRepository testResourceGroupModelRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${server.domain}")
    private String domain;

    @Override
    public void notifyToIcivet(TestTracking... trackingList) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);

        Map<String, List<TestResourceGroup>> resourceGroupMap = testResourceGroupRepository.findAll()
                .stream().collect(Collectors.groupingBy(TestResourceGroup::getUniqueKey));

        Map<String, String> resourceGroupModelMap = testResourceGroupModelRepository.findAll().stream().collect(Collectors.toMap(
                resourceGroupModel -> resourceGroupModel.getFactory() + "_" + resourceGroupModel.getSectionName() + "_" + resourceGroupModel.getModelName(),
                resourceGroupModel -> resourceGroupModel.getFactory() + "_" + resourceGroupModel.getSectionName() + "_" + resourceGroupModel.getResourceGroupName(),
                (r1, r2) -> r1));

        for (TestTracking tracking : trackingList) {
            if ("B05".equalsIgnoreCase(tracking.getFactory()) ||
                    tracking.getType() == TestTracking.Type.WARNING_IDLE ||
                    tracking.getStatus() == TestTracking.Status.TIMEOUT) {
                continue;
            }

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(domain + "/icivet/task/handle")
                    .queryParam("trackingId", tracking.getId());

            IcivetNewsMessage.IcivetArticle article = IcivetNewsMessage.IcivetArticle.of(
                    "WS - VN FII Team",
                    String.format("[%s] %s - %s", tracking.getTrackingCode(), tracking.getStationName(), tracking.getType()),
                    tracking.getMessage(),
                    uriBuilder.toUriString(),
                    tracking.isLockedType() ? "528b7f9b-a550-4ec3-a587-585218ca86ee" : "414e19f9-b898-4511-b512-a96f05162ae9",
                    null);

            String resourceGroupModel = resourceGroupModelMap.getOrDefault(tracking.getFactory() + "_" + tracking.getSectionName() + "_" + tracking.getModelName(), tracking.getFactory() + "_" + tracking.getSectionName() + "_ALL");
            List<TestResourceGroup> resourceGroupList = resourceGroupMap.get(resourceGroupModel + "_1");
            if (resourceGroupList == null || resourceGroupList.isEmpty()) {
                log.error("### notifyToIcivet send resourceGroupList empty factory {}, modelName {}", tracking.getFactory(), tracking.getModelName());
                return;
            }

            for (TestResourceGroup resourceGroup : resourceGroupList) {
                sendToIcivet(resourceGroup.getFactory(), resourceGroup.getIcivetGroupId(), IcivetNewsMessage.of(Collections.singletonList(article)));
            }
        }
    }

    @Override
    public void notifyToIcivet(String message, TestTracking tracking) {
        Map<String, List<TestResourceGroup>> resourceGroupMap = testResourceGroupRepository.findAll()
                .stream().collect(Collectors.groupingBy(TestResourceGroup::getUniqueKey));

        Map<String, String> resourceGroupModelMap = testResourceGroupModelRepository.findAll()
                .stream().collect(Collectors.toMap(
                        resourceGroupModel -> resourceGroupModel.getFactory() + "_" + resourceGroupModel.getSectionName() + "_" + resourceGroupModel.getModelName(),
                        resourceGroupModel -> resourceGroupModel.getFactory() + "_" + resourceGroupModel.getSectionName() + "_" + resourceGroupModel.getResourceGroupName(),
                        (r1, r2) -> r1));

        String resourceGroupModel = resourceGroupModelMap.getOrDefault(tracking.getFactory() + "_" + tracking.getSectionName() + "_" + tracking.getModelName(), tracking.getFactory() + "_" + tracking.getSectionName() + "_ALL");
        List<TestResourceGroup> resourceGroupList = resourceGroupMap.get(resourceGroupModel + "_1");
        if (resourceGroupList == null || resourceGroupList.isEmpty()) {
            log.error("### notifyToIcivet send resourceGroupList empty factory {}, modelName {}", tracking.getFactory(), tracking.getModelName());
            return;
        }

        for (TestResourceGroup resourceGroup : resourceGroupList) {
            sendToIcivet(resourceGroup.getFactory(), resourceGroup.getIcivetGroupId(), IcivetTextMessage.of(message));
        }
    }

    private void sendToIcivet(String from, Integer toGroup, IcivetTextMessage data) {
        try {
            String json = mapper.writeValueAsString(data);
            String message = mapper.writeValueAsString(NotifyMessage.of(
                    NotifyMessage.System.CIVET,
                    NotifyMessage.Type.TEXT,
                    "WS",
                    from,
                    toGroup,
                    json));

            amqpTemplate.convertAndSend("notify", "", message);
        } catch (Exception e) {
            log.error("### sendToIcivet error", e);
        }
    }

    private void sendToIcivet(String from, Integer toGroup, IcivetNewsMessage data) {
        try {
            String json = mapper.writeValueAsString(data);
            String message = mapper.writeValueAsString(NotifyMessage.of(
                    NotifyMessage.System.CIVET,
                    NotifyMessage.Type.NEWS,
                    "WS",
                    from,
                    toGroup,
                    json));

            amqpTemplate.convertAndSend("notify", "", message);
        } catch (Exception e) {
            log.error("### sendToIcivet error", e);
        }
    }

    @Override
    public void notifyToMail(MailMessage data, String from, String to) {
        try {
            String json = mapper.writeValueAsString(data);
            String message = mapper.writeValueAsString(NotifyMessage.of(
                    NotifyMessage.System.MAIL,
                    NotifyMessage.Type.TEXT,
                    "WS",
                    from,
                    to,
                    json));

            amqpTemplate.convertAndSend("notify", "", message);
        } catch (Exception e) {
            log.error("### sendToIcivet error", e);
        }
    }

}
