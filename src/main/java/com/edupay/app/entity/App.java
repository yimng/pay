package com.edupay.app.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "bit_app")
public class App implements Serializable {
    /**
     * 应用ID
     */
    @Id
    @Column(name = "app_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appId;

    /**
     * 应用名称
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * 应用地址
     */
    private String url;

    /**
     * 密钥
     */
    private String token;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 创建人
     */
    @Column(name = "create_uid")
    private String createUid;

    /**
     * 状态：1.启用；2.禁用;
     */
    private Integer state;

    private static final long serialVersionUID = 1L;

    /**
     * 获取应用ID
     *
     * @return app_id - 应用ID
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * 设置应用ID
     *
     * @param appId 应用ID
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * 获取应用名称
     *
     * @return app_name - 应用名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置应用名称
     *
     * @param appName 应用名称
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取应用地址
     *
     * @return url - 应用地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置应用地址
     *
     * @param url 应用地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取密钥
     *
     * @return token - 密钥
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置密钥
     *
     * @param token 密钥
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取创建人
     *
     * @return create_uid - 创建人
     */
    public String getCreateUid() {
        return createUid;
    }

    /**
     * 设置创建人
     *
     * @param createUid 创建人
     */
    public void setCreateUid(String createUid) {
        this.createUid = createUid;
    }

    /**
     * 获取状态：1.启用；2.禁用;
     *
     * @return state - 状态：1.启用；2.禁用;
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1.启用；2.禁用;
     *
     * @param state 状态：1.启用；2.禁用;
     */
    public void setState(Integer state) {
        this.state = state;
    }
}