<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div ng-controller="VIPCtrl as sctrl" style="text-align: center;">
	<div class="btn-group" role="group" >
		<button ng-disabled="sctrl.buttonControl || !sctrl.verify() " type="button" style="width: 12em;height: 1.8em;margin-top: 0.2em;line-height: 1em;" class="btn btn-sm btn-success" ng-click="sctrl.action='add'" data-toggle="modal" data-target="#vipCheck">建立VIP</button>
		<button ng-disabled="sctrl.buttonControl || sctrl.verify() " type="button" style="width: 12em;height: 1.8em;margin-top: 0.2em;line-height: 1em;" class="btn btn-sm btn-danger" ng-click="sctrl.action='delete'" data-toggle="modal" data-target="#vipCheck">取消VIP</button>
	</div>
	<button ng-disabled="sctrl.buttonControl || !sctrl.data.msg" type="button" style="width: 12em;height: 1.8em;margin-top: 0.2em;line-height: 1em;margin-left: 2em;" class="btn btn-sm btn-danger" data-toggle="modal" data-target="#vipSMSCheck">發送通知簡訊</button>
	<table class="dataTable table table-sm" style="text-align:  center; width: 98%;margin: 0 1% 0 1%;    line-height: 1em;margin-top: 2em;">
	  <thead>
	    <tr class="table-active">
	      <th scope="col" ng-repeat="h in sctrl.header" width="{{h.size}}">{{h.title}}</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr ng-repeat="d in sctrl.data.list">
	      <td ng-repeat="h in sctrl.header" >{{d[h.value]}}</td>
	    </tr>
	  </tbody>
	</table>
	<modal mid="vipCheck" title="sendCheck" width="20em" yes-function="sctrl.query(sctrl.action)">
		<div>確定執行？	</div>
	</modal>
	<modal mid="vipSMSCheck" title="sendCheck" width="20em" yes-function="sctrl.sendSMS()">
		<div>確定發送通知簡訊給用戶({{sctrl.msisdn}})？</div>
	</modal>
</div>