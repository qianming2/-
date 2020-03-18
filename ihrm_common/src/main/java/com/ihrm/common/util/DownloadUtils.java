package com.ihrm.common.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DownloadUtils {
    public void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response,String returnName) throws IOException {
        response.setContentType("application/octet-steam");
        response.encodeURL(new String(returnName.getBytes(),"iso8859-1"));//保存的文件名必须和页面编码一致
        response.addHeader("content-disposition","attachment;filename"+returnName);
        response.setContentLength(byteArrayOutputStream.size());
        ServletOutputStream outputStream = response.getOutputStream(); //取的输出流
        byteArrayOutputStream.writeTo(outputStream);                   //写到输出流
        byteArrayOutputStream.close();                                 //关闭
        outputStream.flush();                                          //刷新
    }
}
