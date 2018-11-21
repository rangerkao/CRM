<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div ng-controller="DataOpenSMSCtrl as sctrl">
	<div>
		<div class="btn-group" role="group" >
			<button type="button" style="width: 6em;height: 1.8em;margin-top: 0.2em;line-height: 1em;" class="btn btn-sm {{sctrl.selecteType=='android'?'btn-primary':'btn-secondary'}}" ng-click="sctrl.selecteType='android'">Android</button>
			<button type="button" style="width: 6em;height: 1.8em;margin-top: 0.2em;line-height: 1em;" class="btn btn-sm {{sctrl.selecteType=='iphone'?'btn-primary':'btn-secondary'}}" ng-click="sctrl.selecteType='iphone'">iPhone</button>
		</div>
		<button ng-disabled="!sctrl.msisdn " type="button" style="width: 12em;height: 1.8em;margin-top: 0.2em;line-height: 1em;margin-left: 2em;" class="btn btn-sm btn-danger" data-toggle="modal" data-target="#sendCheck">Send{{sctrl.msisdn?'     ('+sctrl.msisdn+')':''}}</button>
		<input ng-show="ctrl.role=='admin'" style="text" ng-model="sctrl.msisdn" value="886989235253">
		<font ng-show="!sctrl.msisdn" color="red">無可發送門號</font>
	</div>

	<table ng-show="sctrl.smsContent" class="dataTable table table-sm" style="text-align:  center; width: 98%;margin: 0 1% 0 1%;    line-height: 1em;background-color: #6c757d; margin-top: 2em;text-align: left;">
		<tr style="color: white;"><th>A</th></tr>
		<tr><td>{{sctrl.smsContent.typeA}}</td></tr>
		<tr style="color: white;"><th>B</th></tr>
		<tr><td>{{sctrl.smsContent.typeB}}</td></tr>
		<tr ng-show="sctrl.selecteType=='android'" style="color: white;"><th>C(Android)</th></tr>
		<tr ng-show="sctrl.selecteType=='android'" ><td>{{sctrl.smsContent.typeC.android}}</td></tr>
		<tr ng-show="sctrl.selecteType=='iphone'" style="color: white;"><th>C(iPhone)</th></tr>
		<tr ng-show="sctrl.selecteType=='iphone'"><td>{{sctrl.smsContent.typeC.iphone}}</td></tr>
	</table>
	
	<modal mid="sendCheck" title="sendCheck" width="20em" yes-function="sctrl.send()">
		<div>
			確定要送出簡訊嗎？
		</div>
	</madel>
</div>