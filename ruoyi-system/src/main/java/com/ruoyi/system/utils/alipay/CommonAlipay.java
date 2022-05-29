package com.ruoyi.system.utils.alipay;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ruoyi.common.config.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月05日 17:47:00
 */
@Component
public class CommonAlipay {

    Logger logger = LoggerFactory.getLogger(CommonAlipay.class);
    @Autowired
    private PropertiesConfig propertiesConfig;

    private AlipayClient defaultAlipayClient;

    /**
     * 阿里云通用支付
     *
     * @param goodsName 商品名称
     * @param money     金额
     * @param goodsId   商品id
     * @return 交易的二维码
     * @throws JSONException      json格式化问题
     * @throws AlipayApiException ali支付问题
     */
    public String createOrder(String goodsName, String money, String goodsId) throws JSONException, AlipayApiException {
        if (defaultAlipayClient == null) {
            getDefaultAlipayClient();
        }
        AlipayTradePrecreateRequest alipayTradePrecreateRequest = new AlipayTradePrecreateRequest();
        //设置回调地址
        alipayTradePrecreateRequest.setNotifyUrl("");
        //设置商品信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", goodsId);
        jsonObject.put("total_amount", money);
        jsonObject.put("subject", goodsName);
        //订单超时的时间
        jsonObject.put("timeout_express", "15m");
        // 加入订单信息
        alipayTradePrecreateRequest.setBizContent(jsonObject.toString());
        // 发起请求
        AlipayTradePrecreateResponse response = defaultAlipayClient.execute(alipayTradePrecreateRequest);
        logger.info(response.getBody());
        // 订单创建结果
        if (response.isSuccess()) {
            return response.getQrCode();
        }
        return "订单创建失败";
    }

    /**
     * 获取订单交易情况
     *
     * @param outTradeNo 订单号
     * @return -1 1 2 3 4
     * @throws AlipayApiException 阿里接口异常
     */
    public String hasOverOrder(String outTradeNo) throws AlipayApiException {
        if (defaultAlipayClient == null) {
            getDefaultAlipayClient();
        }
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        try {
            bizContent.put("out_trade_no", outTradeNo);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return "";
        }
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = defaultAlipayClient.execute(request);
        // 查询成功怎么处理
        if (response.isSuccess()) {
           return PayStatus.payInfo(response.getTradeStatus());
        }
        logger.error("订单查询失败，请检查:"+response.getSubMsg());
        return "";
    }

    /**
     * 支付宝退款
     * @param outTradeNo 支付宝订单id
     * @param money 退款金额
     * @param refundReason 退款原有
     * @return 退款code
     * @throws AlipayApiException 支付宝接口错误信息
     */
    public int reFunRequest(String outTradeNo, String tradeNo, String money, String refundReason, String outRequestNo) throws AlipayApiException {
        if(defaultAlipayClient==null){
            getDefaultAlipayClient();
        }
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        try {
            bizContent.put("trade_no",tradeNo);
            bizContent.put("out_trade_no",outTradeNo);
            bizContent.put("refund_amount",money);
            bizContent.put("refund_reason",refundReason);
            bizContent.put("out_request_no",outRequestNo);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return -1;
        }
        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response = defaultAlipayClient.execute(request);
        if(response.isSuccess()){
            return 1;
        }
        logger.error("退款失败，请检查:"+response.getSubMsg());
        return -1;
    }

    /**
     * 退款接口查询
     * @param outTradeNo 订单id
     * @param outRequestNo 退款订单号
     * @return 退款结果
     * @throws JSONException json异常
     * @throws AlipayApiException 阿里接口异常
     */
    public int hasReFund(String outTradeNo, String outRequestNo) throws JSONException, AlipayApiException {
        if(defaultAlipayClient==null){
            getDefaultAlipayClient();
        }
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no",outTradeNo);
        bizContent.put("out_request_no",outRequestNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeFastpayRefundQueryResponse response = defaultAlipayClient.execute(request);
        if(response.isSuccess()){
            if("REFUND_SUCCESS".equals(response.getRefundStatus())){
                System.out.println("退款成功");
                return 1;
            }
            logger.warn(response.getSubMsg());
            return 2;
        }
        logger.error("查询退款失败:"+response.getSubMsg());
        return -1;
    }

    /**
     * 获取默认的阿里支付客户端
     */
    private void getDefaultAlipayClient() {
        defaultAlipayClient = new DefaultAlipayClient(propertiesConfig.getAliUrl(), propertiesConfig.getAPP_ID()
                , propertiesConfig.getMER_PRIVATE_KEY(), "json",
                "utf-8", propertiesConfig.getPUBLIC_KEY(), "RSA2");
    }
}
