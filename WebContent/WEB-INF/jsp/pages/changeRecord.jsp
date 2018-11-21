<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div ng-controller="ChangeRecordCtrl as sctrl" style="text-align: center;">
	<div>
		<button ng-disabled="sctrl.buttonControl||!sctrl.custInfo" type="button" style="width: 12em;height: 1.8em;margin-top: 0.2em;line-height: 1em;margin-left: 2em;" class="btn btn-sm btn-primary" ng-click="sctrl.queryCardRecord()">查詢換卡記錄</button>
		{{(sctrl.custInfo && sctrl.buttonControl?'查詢中...':'')}}
	</div>
	<table class="dataTable table table-sm" ng-show="sctrl.cardData" style="text-align:  center; width: 99%;display:  inline-table;    line-height: 1em;margin-top: 2em;">
	  <thead>
	    <tr class="table-active">
	      <th scope="col" ng-repeat="h in sctrl.cardHeader" style="width:{{h.size}}">{{h.title}}</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr ng-repeat="d in sctrl.cardData">
	      <td ng-repeat="h in sctrl.cardHeader" >{{d[h.value]}}</td>
	    </tr>
	  </tbody>
	</table>
	<div>
		<button ng-disabled="sctrl.buttonControl||!sctrl.custInfo" type="button" style="width: 12em;height: 1.8em;margin-top: 0.2em;line-height: 1em;margin-left: 2em;" class="btn btn-sm btn-primary" ng-click="sctrl.queryNumberRecord()">查詢換號記錄</button>
		{{(sctrl.custInfo && sctrl.buttonControl?'查詢中...':'')}}
	</div>
	<table class="dataTable table table-sm" ng-show="sctrl.numberData" style="text-align:  center; width: 98%;margin: 0 1% 0 1%;    line-height: 1em;margin-top: 2em;">
	  <thead>
	    <tr class="table-active">
	      <th scope="col" ng-repeat="h in sctrl.numberHeader" style="width:{{h.size}}">{{h.title}}</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr ng-repeat="d in sctrl.numberData">
	      <td ng-repeat="h in sctrl.numberHeader" >{{d[h.value]}}</td>
	    </tr>
	  </tbody>
	</table>
</div>