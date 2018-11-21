package com.bean;

import java.util.List;

public class Subscriber {
	
	String name = "";
	String birthday = "";
	String idTaxid = "";
	String phone = "";
	String email = "";
	String permanentAddress = "";
	String billingAddress = "";
	String agency = "";
	String remark = "";
	String type = "";
	String createtime = "";
	String updatetime = "";
	
	//20170317 add
	String passportId = "";
	String passportName = "";
	
	String s2tMsisdn = "";
	String chtMsisdn = "";
	String serviceId = "";
	
	String chair = "";
	String chairId = "";
	
	String s2tIMSI = "";
	PricePlanID pricePlanId = null;
	
	String status = "";
	
	String activatedDate = "";
	String canceledDate = "";
	
	String seq="";
	
	String homeIMSI="";
	
	
	String nowS2tActivated = "0";
	
	List<String> vlns;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getIdTaxid() {
		return idTaxid;
	}
	public void setIdTaxid(String idTaxid) {
		this.idTaxid = idTaxid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPermanentAddress() {
		return permanentAddress;
	}
	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getS2tMsisdn() {
		return s2tMsisdn;
	}
	public void setS2tMsisdn(String s2tMsisdn) {
		this.s2tMsisdn = s2tMsisdn;
	}
	public String getChtMsisdn() {
		return chtMsisdn;
	}
	public void setChtMsisdn(String chtMsisdn) {
		this.chtMsisdn = chtMsisdn;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getChair() {
		return chair;
	}

	public void setChair(String chair) {
		this.chair = chair;
	}

	public String getChairId() {
		return chairId;
	}

	public void setChairId(String chairId) {
		this.chairId = chairId;
	}

	public String getS2tIMSI() {
		return s2tIMSI;
	}

	public void setS2tIMSI(String s2tIMSI) {
		this.s2tIMSI = s2tIMSI;
	}

	
	public PricePlanID getPricePlanId() {
		return pricePlanId;
	}
	
	public void setPricePlanId(PricePlanID pricePlanId) {
		this.pricePlanId = pricePlanId;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivatedDate() {
		return activatedDate;
	}

	public void setActivatedDate(String activatedDate) {
		this.activatedDate = activatedDate;
	}

	public String getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(String canceledDate) {
		this.canceledDate = canceledDate;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getHomeIMSI() {
		return homeIMSI;
	}

	public void setHomeIMSI(String homeIMSI) {
		this.homeIMSI = homeIMSI;
	}

	public String getPassportId() {
		return passportId;
	}

	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}

	public String getPassportName() {
		return passportName;
	}

	public void setPassportName(String passportName) {
		this.passportName = passportName;
	}

	public String getNowS2tActivated() {
		return nowS2tActivated;
	}

	public void setNowS2tActivated(String nowS2tActivated) {
		this.nowS2tActivated = nowS2tActivated;
	}
	
	public List<String> getVlns() {
		return vlns;
	}
	public void setVlns(List<String> vlns) {
		this.vlns = vlns;
	}

	
	
	
	

	
}
