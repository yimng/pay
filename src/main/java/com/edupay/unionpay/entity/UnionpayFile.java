package com.edupay.unionpay.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "bit_unionpay_file")
public class UnionpayFile implements Serializable {
    /**
     * 银联交易文件id
     */
    @Id
    @Column(name = "unionpay_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    private String unionpayFileId;

    /**
     * 银联文件下载地址
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * 状态:0 接受成功;1导入数据库成功;3导入失败
     */
    private Integer status;

    /**
     * 对账日;年月日每天一条
     */
    @Column(name = "check_date")
    private Date checkDate;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    private static final long serialVersionUID = 1L;

    /**
     * 获取银联交易文件id
     *
     * @return unionpay_file_id - 银联交易文件id
     */
    public String getUnionpayFileId() {
        return unionpayFileId;
    }

    /**
     * 设置银联交易文件id
     *
     * @param unionpayFileId 银联交易文件id
     */
    public void setUnionpayFileId(String unionpayFileId) {
        this.unionpayFileId = unionpayFileId;
    }

    /**
     * 获取银联文件下载地址
     *
     * @return file_url - 银联文件下载地址
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置银联文件下载地址
     *
     * @param fileUrl 银联文件下载地址
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取状态:0 接受成功;1导入数据库成功;3导入失败
     *
     * @return status - 状态:0 接受成功;1导入数据库成功;3导入失败
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态:0 接受成功;1导入数据库成功;3导入失败
     *
     * @param status 状态:0 接受成功;1导入数据库成功;3导入失败
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取对账日;年月日每天一条
     *
     * @return check_date - 对账日;年月日每天一条
     */
    public Date getCheckDate() {
        return checkDate;
    }

    /**
     * 设置对账日;年月日每天一条
     *
     * @param checkDate 对账日;年月日每天一条
     */
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", unionpayFileId=").append(unionpayFileId);
        sb.append(", fileUrl=").append(fileUrl);
        sb.append(", status=").append(status);
        sb.append(", checkDate=").append(checkDate);
        sb.append(", createDate=").append(createDate);
        sb.append("]");
        return sb.toString();
    }
}