package com.legal.app.controller;

import com.common.log.BusinessException;
import com.common.utils.StringUtil;
import com.common.web.Pagination4Datatable;
import com.common.web.Paging;
import com.common.web.WebUtils;
import com.legal.app.model.Message;
import com.legal.app.model.MessageSelectParam;
import com.legal.app.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/message")
@Api(tags = "系统管理-消息推送")
public class MessageController extends SuperController{
    @Autowired
    private IMessageService MessageService;

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{ \"addTimeEnd\": \"2018-09-10\", \"addTimeStart\": \"2018-09-07\", \"state\": 1, \"content\": \"我的\", \"start\": 0, \"length\": 2 }", responseContainer = "Map")
    @RequestMapping(value = "list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable execute(MessageSelectParam messageSelectParam) {
        Integer start = messageSelectParam.getStart();
        Integer length = messageSelectParam.getLength();
        int currentPage = start / length + 1;
        List<Message> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(length, currentPage, true);
        try {
            list = MessageService.list(messageSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Message> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(length);
        pagingResult.setStart(currentPage * length);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, messageSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "列表", notes = "{ \"addTimeEnd\": \"2018-09-10\", \"addTimeStart\": \"2018-09-07\", \"state\": 1, \"content\": \"我的\", \"start\": 0, \"length\": 2 }", responseContainer = "Map")
    @RequestMapping(value = "params", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Pagination4Datatable params(@RequestBody MessageSelectParam messageSelectParam) {
        Integer start = messageSelectParam.getStart();
        Integer length = messageSelectParam.getLength();
        int currentPage = start / length + 1;
        List<Message> list = new ArrayList<>();
        com.common.dbutil.Paging paging = new com.common.dbutil.Paging(length, currentPage, true);
        try {
            list = MessageService.list(messageSelectParam, paging);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paging<Message> pagingResult = new Paging<>();
        pagingResult.setList(list);
        pagingResult.setRowCount(paging.getTotalCount());
        pagingResult.setLimit(length);
        pagingResult.setStart(currentPage * length);
        pagingResult.setCurrentPage(currentPage);

        return Pagination4Datatable.getInstanceMap(pagingResult, messageSelectParam.getsEcho());
    }

    @ResponseBody
    @ApiOperation(value = "增加", notes = "{\n" +
            "  \"addresser\": \"你的\",\n" +
            "  \"businessId\": 1,\n" +
            "  \"businessType\": \"我的\",\n" +
            "  \"isDelete\": 1,\n" +
            "  \"isSend\": 1,\n" +
            "  \"messageContent\": \"我的内容\",\n" +
            "  \"messageState\": 2,\n" +
            "  \"sendTime\": \"2018-09-10\",\n" +
            "  \"title\": \"我的\",\n" +
            "  \"userId\": 1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void add(@RequestBody Message message, PrintWriter out) {
        try {
            MessageService.add(message);
            out.print(WebUtils.responseData(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "详情", responseContainer = "Map")
    @RequestMapping(value = "info/{messageId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void info(@PathVariable Integer messageId, PrintWriter out) {
        try {
            Message info = new Message();
            info.setMessageId(messageId);
            info = MessageService.info(info);
            out.print(WebUtils.responseData(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "编辑", notes = "{\n" +
            "  \"columnType\": 2,\n" +
            "  \"content\": \"2\",\n" +
            "  \"image\": \"2\",\n" +
            "  \"name\": \"2\",\n" +
            "  \"place\": \"2\",\n" +
            "  \"url\": \"2\",\n" +
            "  \"id\": 1\n" +
            "}", responseContainer = "Map")
    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void update(@RequestBody Message message, PrintWriter out) {
        try {
            MessageService.update(message);
            out.print(WebUtils.responseData(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除", responseContainer = "Map")
    @RequestMapping(value = "remove/{messageId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void remove(@PathVariable Integer messageId, PrintWriter out) {
        try {
            MessageService.remove(messageId);
            out.print(WebUtils.responseData(messageId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @ApiOperation(value = "发送", responseContainer = "Map")
    @RequestMapping(value = "send/{messageId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void send(@PathVariable Integer messageId, PrintWriter out) {
        try {
            MessageService.send(messageId);
            out.print(WebUtils.responseData(messageId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 站内消息发送
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("messageSend")
    public void messageSend(HttpServletRequest request,PrintWriter out)throws Exception{
     String businessType = request.getParameter("businessType"); //0：回复消息 1：系统消息
     String userId = request.getParameter("userId");
     if(StringUtil.isBlank(String.valueOf(userId))){
      throw new BusinessException("用户编号不能缺少.",-1);
     }
     String messageContent = request.getParameter("messageContent");
     if(StringUtil.isBlank(String.valueOf(messageContent))){
      throw new BusinessException("消息内容不能缺少.",-1);
     }

     if(StringUtil.isBlank(String.valueOf(businessType))){  
      throw new BusinessException("业务类型不能缺少.",-1);
     }
     String businessId = request.getParameter("businessId");
     if(StringUtil.isBlank(String.valueOf(businessId))){
      throw new BusinessException("业务编号不能缺少.",-1);
     }
     String messageType = request.getParameter("messageType");
     if(StringUtil.isBlank(String.valueOf(messageType))){
      throw new BusinessException("消息类型.",-1);
     }
     Map map = getMap(request);
     //map.put("messageContent",request.getParameter("messageType")+request.getParameter("messageContent")); //0:没读 1：已读
     map.put("isRead","0"); //0:没读 1：已读
     map.put("sendTime",new Date()); //0:没读 1：已读
     map.put("isDelete","0"); //0:未删除 1：已删除
     map.put("messageState","0"); // 
     map.put("is_send","1"); //0:未发送1：已发送
     map.put("create_time",new Date()); 
     
     MessageService.messageSend(map);
     out.print(WebUtils.responseData(map));
    }
}
