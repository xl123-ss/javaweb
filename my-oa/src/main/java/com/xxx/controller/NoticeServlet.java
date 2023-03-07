package com.xxx.controller;

import com.xxx.entity.Notice;
import com.xxx.service.NoticeService;
import com.xxx.vto.R;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/notice/list")
public class NoticeServlet extends HttpServlet {

    NoticeService noticeService = new NoticeService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * 换回通知集合
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eid = req.getParameter("eid");
        R r = null;
        try {

            List<Notice> noticeList = noticeService.getNoticeList(Long.parseLong(eid));
            r = R.ok().data("list",noticeList);
        } catch (Exception e) {
            e.printStackTrace();
            r = R.error().data("smg", e.getMessage());
        }
        resp.getWriter().println(r.toJsonStr());
    }
}
