package org.igov.service.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.EmailException;
import org.igov.io.mail.Mail;
import org.igov.service.business.action.task.core.ActionTaskService;
import org.igov.service.exception.CRCInvalidException;
import org.igov.service.exception.CommonServiceException;
import org.igov.service.exception.RecordNotFoundException;
import org.igov.service.exception.TaskAlreadyUnboundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.igov.service.business.flow.FlowService;
import org.igov.service.business.flow.slot.ClearSlotsResult;
import org.igov.service.business.flow.slot.Days;
import org.igov.service.business.flow.slot.Day;
import org.igov.service.business.flow.slot.FlowSlotVO;
import org.joda.time.DateTime;

/**
 * @author BW
 */
@Controller
@Api(tags = {"DebugCommonController - Дебаг и тест общий"})
public class DebugCommonController {

    private static final Logger LOG = LoggerFactory
            .getLogger(DebugCommonController.class);

    public static final int DAYS_IN_MONTH = 30;
    public static final int WORK_DAYS_NEEDED = 20;
    public static final int DAYS_IN_HALF_YEAR = 180;

    @Autowired
    private FlowService oFlowService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private Mail oMail;

    @Autowired
    private ActionTaskService oActionTaskService;

    @ApiOperation(value = "/test/action/task/delete-processTest", notes = "#####  DebugCommonController: описания нет\n")
    @RequestMapping(value = "/test/action/task/delete-processTest", method = RequestMethod.GET)
    public @ResponseBody
    void deleteProcessTest(@RequestParam(value = "sProcessInstanceID") String processInstanceID,
            @RequestParam(value = "sLogin", required = false) String sLogin,
            @RequestParam(value = "sReason", required = false) String sReason
    ) throws Exception {
        runtimeService.deleteProcessInstance(processInstanceID, sReason);
    }

    @ApiOperation(value = "/test/sendAttachmentsByMail", notes = "#####  DebugCommonController: описания нет\n")
    @RequestMapping(value = "/test/sendAttachmentsByMail", method = RequestMethod.GET)
    @Transactional
    public void sendAttachmentsByMail(
            @RequestParam(value = "sMailTo", required = false) String sMailTo,
            @RequestParam(value = "nID_Task", required = false) String snID_Task,
            @RequestParam(value = "sBody", required = false) String sBody,
            @RequestParam(value = "bHTML", required = false) boolean bHTML,
            @RequestParam(value = "naID_Attachment", required = false) String snaID_Attachment,
            @RequestParam(value = "bUnisender", required = false) Boolean bUnisender)
            throws IOException, MessagingException, EmailException {

        oMail._To("bvv4ik@gmail.com");
        oMail._Body(sBody == null ? "<a href=\"http:\\\\google.com\">Google</a> It's test Проверка ! ��� ��������!"
                : sBody);

        LOG.info("(oMail.getHead()={})", oMail.getHead());
        LOG.info("(oMail.getBody()={})", oMail.getBody());
        LOG.info("(oMail.getAuthUser()={})", oMail.getAuthUser());
        LOG.info("(oMail.getAuthPassword()={})", oMail.getAuthPassword());
        LOG.info("(oMail.getFrom()={})", oMail.getFrom());
        LOG.info("(oMail.getTo()={})", oMail.getTo());
        LOG.info("(oMail.getHost()={})", oMail.getHost());
        LOG.info("(oMail.getPort()={})", oMail.getPort());

        if (snaID_Attachment != null) {
            String[] ansID_Attachment = snaID_Attachment.split(",");
            for (String snID_Attachment : ansID_Attachment) {
                Attachment oAttachment = taskService
                        .getAttachment(snID_Attachment);
                String sFileName = oAttachment.getName();
                String sFileExt = oAttachment.getType().split(";")[0];
                String sDescription = oAttachment.getDescription();
                LOG.info("(oAttachment.getId()={}, sFileName={}, sFileExt={}, sDescription={}",
                        oAttachment.getId(), sFileName, sFileExt, sDescription);
                InputStream oInputStream = taskService
                        .getAttachmentContent(oAttachment.getId());
                DataSource oDataSource = new ByteArrayDataSource(oInputStream,
                        sFileExt);

                oMail._Attach(oDataSource, sFileName + "." + sFileExt,
                        sDescription);
            }
        }

        if (bUnisender != null && bUnisender) {
            oMail.sendWithUniSender();
        } else {
            oMail.send();
        }
    }

    @Deprecated
    //Нужно будет удалить после недели работы продеплоеной в прод версии (для обратной временной совместимости)
    @ApiOperation(value = "/rest/tasks/cancelTask", notes = "#####  DebugCommonController:\n")
    @RequestMapping(value = "/rest/tasks/cancelTask", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    ResponseEntity<String> cancelTask(
            @ApiParam(value = "", required = true) @RequestParam(value = "nID_Protected") Long nID_Protected,
            @ApiParam(value = "", required = false) @RequestParam(value = "sInfo", required = false) String sInfo)
            throws CommonServiceException, TaskAlreadyUnboundException {

        String sMessage = "Ваша заявка відмінена. Ви можете подати нову на Порталі державних послуг iGov.org.ua.<\n<br>"
                + "З повагою, команда порталу  iGov.org.ua";

        try {
            oActionTaskService.cancelTasksInternal(nID_Protected, sInfo);
            return new ResponseEntity<String>(sMessage, HttpStatus.OK);
        } catch (CRCInvalidException | RecordNotFoundException e) {
            CommonServiceException newErr = new CommonServiceException(
                    "BUSINESS_ERR", e.getMessage(), e);
            newErr.setHttpStatus(HttpStatus.FORBIDDEN);
            LOG.warn("Error: {}", e.getMessage());
            LOG.trace("FAIL:", e);
            sMessage = "Вибачте, виникла помилка при виконанні операції. Спробуйте ще раз, будь ласка";

            return new ResponseEntity<String>(sMessage, HttpStatus.FORBIDDEN);
        }

    }

    @ApiOperation(value = "/test/action/getInfo", notes = "#####  DebugCommonController: \n")
    @RequestMapping(value = "/test/action/getInfo", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String getInfo(@ApiParam(value = "", required = false)
            @RequestParam(value = "sID_TestType", required = false) String sID_TestType
    ) {

        return "successfull";

    }

    //maxline: тестирование работы получения свободных слотов getFlowSlots findFlowSlotsByFlow и в случае отсутствия 
    //генерация новых слотов buildFlowSlots
    @ApiOperation(value = "/test/action/testSheduleBuilderFlowSlots", notes = "#####  DebugCommonController: описания нет\n")
    @RequestMapping(value = "/test/action/testSheduleBuilderFlowSlots", method = RequestMethod.GET)
    public @ResponseBody
    void testSheduleBuilderFlowSlots(
            @RequestParam(value = "nID_Flow_ServiceData", required = false) Long nID_Flow_ServiceData,
            @RequestParam(value = "nID_ServiceData", required = false) Long nID_ServiceData,
            @RequestParam(value = "sDateStart", required = false) String sDateStart,
            @RequestParam(value = "sDateStop", required = false) String sDateStop,
            @RequestParam(value = "bAll", required = false) boolean bAll,
            @RequestParam(value = "nFreeDays", required = false, defaultValue = "30") int nFreeDaysNeeded, //Maxline: TODO не используется?
            @RequestParam(value = "nDays", required = false, defaultValue = "180") int nDays,
            @RequestParam(value = "sOperation", required = false) String sOperation) throws Exception {
        LOG.info("/test/action/testSheduleBuilderFlowSlots  - invoked");

        DateTime oDateStart;
        DateTime oDateEnd;

        //Maxline: TODO добавить исключения
        nID_Flow_ServiceData = (nID_Flow_ServiceData == null) ? 12L : nID_Flow_ServiceData; //_test_queue_cancel
        //nFreeDays = (nFreeDays == 0) ? 3 : nFreeDays;
        nID_ServiceData = (nID_ServiceData == null) ? 358L : nID_ServiceData; //_test_queue_cancel
        //Long nID_ServiceData = 63L; //Видача/заміна паспорта громадянина для виїзду за кордон

        if (sDateStart == null || sDateStart.equals("")) {
            //sDateStart = "2016-05-12 00:00:00.000";
            oDateStart = DateTime.now().withTimeAtStartOfDay();
        } else {
            oDateStart = oFlowService.parseJsonDateTimeSerializer(sDateStart);
        }
        if (sOperation == null) {
            sOperation = "";
        }
        if (bAll != true) {  // Maxline: bAll должно быть false в рабочей версии
            bAll = false;
        }
        //boolean bAll = true;
        //        if (sDateStop == null || sDateStop.equals("")) {
        //            //sDateStop = "2016-05-12 00:00:00.000";
        //oDateEnd = oDateStart.plusDays(nDays);
        //        } else {
        //            oDateEnd = oFlowService.parseJsonDateTimeSerializer(sDateStop);
        //        }

        LOG.info("sDateStart = {}", sDateStart);
        LOG.info("sDateStop = {}", sDateStop);
        LOG.info("oDateStart = {}", oDateStart);
        //LOG.info("oDateEnd = {}", oDateEnd);

        switch (sOperation) {
            case "checkAndBuild":
                int nStartDay = 0;
                DateTime dateStart = oDateStart.plusDays(0);
                DateTime dateEnd;

                while (!isFreeDaysEnough(nID_ServiceData, oDateStart) && nStartDay < DAYS_IN_HALF_YEAR) {
                    dateStart = dateStart.plusDays(nStartDay);
                    dateEnd = dateStart.plusDays(nStartDay + DAYS_IN_MONTH);

                    List<FlowSlotVO> resFlowSlotVO = oFlowService.buildFlowSlots(nID_Flow_ServiceData, dateStart, dateEnd);
                    LOG.info("resFlowSlotVO.size() = {}", resFlowSlotVO.size());

                    nStartDay += DAYS_IN_MONTH;
                }
                break;
            case "build":
                List<FlowSlotVO> resFlowSlotVO = oFlowService.buildFlowSlots(nID_Flow_ServiceData, oDateStart, oDateEnd);
                LOG.info("resFlowSlotVO.size() = {}", resFlowSlotVO.size());
                break;
            case "clear":
                boolean bWithTickets = false;
                oFlowService.clearFlowSlots(nID_Flow_ServiceData, oDateStart, oDateEnd, bWithTickets);
                break;
        }

        LOG.info("/test/action/testSheduleBuilderFlowSlots  - exit1");
        //runtimeService.deleteProcessInstance(processInstanceID, sReason);
    }

    private boolean isFreeDaysEnough(Long nID_ServiceData, DateTime oDateStart) {
        int nFreeWorkDaysFact;

        Long nID_Service = null; //176L;
        String sID_BP = null;
        Long nID_SubjectOrganDepartment = null;
        boolean bAll = false;
        DateTime oDateEnd = oDateStart.plusDays(DAYS_IN_HALF_YEAR);
        LOG.info("oDateEnd = {}", oDateEnd);

        Days res = oFlowService.getFlowSlots(nID_Service, nID_ServiceData, sID_BP, nID_SubjectOrganDepartment,
                oDateStart, oDateEnd, bAll, WORK_DAYS_NEEDED);  //Maxline: есть еще nFreeDaysNeeded
        LOG.info("Days = {}", res);

        nFreeWorkDaysFact = res.getaDay().size();
        LOG.info("Days.size() = {}, WORK_DAYS_NEEDED = {}", nFreeWorkDaysFact, WORK_DAYS_NEEDED);
        for (Day day : res.getaDay()) {
            LOG.info("Day = {}, isbHasFree = {}", day.getsDate(), day.isbHasFree());
        }

        return nFreeWorkDaysFact >= WORK_DAYS_NEEDED;
    }

}
