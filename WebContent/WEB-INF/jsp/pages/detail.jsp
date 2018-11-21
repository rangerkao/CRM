<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="detail" ng-controller="DetailCtrl as sctrl">
	<div class="container-fluid row" style="font-size: 14px;font-weight: bold;">
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">S2T IMSI:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.custInfo.s2tIMSI}}</label>
		</div>
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">Home IMSI:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.custInfo.homeIMSI}}</label>
		</div>
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
		</div>
		<!--  -->
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">啟用時間:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.custInfo.activatedDate}}</label>
		</div>
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">退租時間:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.custInfo.canceledDate}}</label>
		</div>
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">登打時間</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.detail.application.dateTime}}</label>
		</div>
		<!--  -->
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">數據狀態:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.detail.gprsStatus!=null?(sctrl.detail.gprsStatus==0?'關閉':'開啟'):''}}</label>
		</div>
		<div  class="col-sm-8 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">VLN:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.detail.vln}}{{sctrl.CHNA}}</label>
		</div>
		<!--  -->
		<div  class="col-sm-4 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">資費:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.custInfo.pricePlanId?sctrl.custInfo.pricePlanId.aliases+'('+sctrl.custInfo.pricePlanId.id+')':''}}</label>
		</div>
		<div  class="col-sm-8 form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">說明:</label>
			<label class="dataValue col" style="margin: 0;">{{sctrl.custInfo.pricePlanId?sctrl.custInfo.pricePlanId.production+':'+sctrl.custInfo.pricePlanId.desc:''}}</label>
		</div>
	</div>
	<div style="margin-top: 1em;">
		<label class="btn btn-sm btn-secondary">加值服務(華人上網包:SX001、SX002、高量SX005；美國流量包:SX003；多國上網包:SX004；美國上網包SX006)</label>
		<table border="1" style="width: 80%;display: inline-table; text-align: center;" >					
			<tr>
				<th>SERVICEID</th><th>SERVICECODE</th><th>STATUS</th><th>STARTDATE</th><th>ENDDATE</th>
			</tr>
			<tr ng-repeat="addon in sctrl.detail.addonservices">
				<td>{{addon.serviceId}}</td><td>{{addon.serviceCode}}</td><td>{{addon.status}}</td><td>{{addon.startDate}}</td><td>{{addon.endDate}}</td>
			</tr>
		</table>
	</div>
</div>