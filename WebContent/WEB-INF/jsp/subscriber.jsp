<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html ng-app="MainApp">
<head>
<%@ include file="header.jsp" %>
<style type="text/css">

</style>
</head>

<body>
<div ng-controller="SubscriberCtrl as ctrl" style="text-align: center;">
	<div><h8>使用者資訊{{ctrl.custInfo.seq}}</h8><a href="logout" class="btn btn-sm btn-warning " style="height: 1.5em;margin-top: 0.2em;line-height: 0.8em;margin-left: 2em;border-radius: 5px;position: absolute;">登出</a></div>
	<div class="btn-group" role="group">
		<button type="button" class="btn btn-sm {{ctrl.selectedCondition==$index?'btn-primary':'btn-secondary'}}" ng-repeat="c in ctrl.conditions" ng-click="ctrl.selectedCondition=$index" style="border-radius: 5px;{{$index==1?'color:#FFBB00;font-weight:bold;':''}}">{{c.name}}</button>
	</div>
	<div style="display:inline-flex;width: 30em;top:+5px;margin-bottom: 10px;position: relative;">
		<input ng-model="ctrl.searchValue" type="text" class="form-control" placeholder="" style="font-size: 20px;font-weight: bold;height: 1.7em;margin: 0;    width: 12em;" ng-keydown="ctrl.onSelectedTypeKeyDown($event)" required>
		<div class="input-group-append">
			<button style="border-radius: 5px;width: 5.5em;" class="btn btn-primary btn-sm" ng-click="ctrl.queryList()">Search</button>
		</div>
	 
		<div class="btn-group" role="group" ng-show="ctrl.selectedCondition==4||ctrl.selectedCondition==5">
		    <button id="btnGroupDrop1" style="border-radius: 5px;width: 5.5em;" class="btn btn-sm btn-secondary" data-toggle="modal" data-target="#userListModal">Show List</button>
			<div class="tipPot" >{{ctrl.custList.length}}</div>
		</div>
		<% out.println("<input id='userRole' type='hidden' value='"+session.getAttribute("s2t.role")+"'>");%>
		
		<button ng-show="ctrl.role=='CSManerger' || ctrl.role=='admin'" style="border-radius: 5px;width: 5.5em;{{ctrl.selectedCondition==4||ctrl.selectedCondition==5?'position: relative;left: -1.4em;':''}}" class="btn btn-success btn-sm" data-toggle="modal" data-target="#dateModal">Download</button>
	</div>	
	<div id="userDataView" class="container-fluid row" style="font-size: 14px;font-weight: bold;">
		<div ng-repeat="d in ctrl.userDataColumn" ng-switch="d.type" class="{{d.size}} form-group row" style="text-align: left;margin-right: 15px;margin: 0;">
			<label class="dataLabel">{{d.label}}:</label>
			
			<label ng-switch-when="button" class="dataValue col" style="margin: 0;"  ng-show="!d.editable||(d.editable&&!ctrl.editMode)" >{{ctrl.custInfo[d.value]!=null ?(ctrl.custInfo[d.value]=='E'?'公司':'個人'):''}}</label>
			<label ng-switch-default class="dataValue col" style="margin: 0;" ng-show="!d.editable||(d.editable&&!ctrl.editMode)" >{{ctrl.custInfo[d.value]}}</label>
			
			<div ng-switch-when="button" class="btn-group" role="group"  ng-show="d.editable&&ctrl.editMode">
				<button type="button" style="width: 3em;height: 1.8em;margin-top: 0.2em;line-height: 1em;" class="btn btn-sm {{ctrl.custInfo[d.value]=='P'?'btn-primary':'btn-secondary'}}" ng-click="ctrl.custInfo[d.value]='P'">個人</button>
				<button type="button" style="width: 3em;height: 1.8em;margin-top: 0.2em;line-height: 1em;" class="btn btn-sm {{ctrl.custInfo[d.value]=='E'?'btn-primary':'btn-secondary'}}" ng-click="ctrl.custInfo[d.value]='E'">公司</button>
			</div>
			<input ng-switch-default class="dataEdit col" style="padding: 0;" ng-show="d.editable&&ctrl.editMode" type="text" ng-model = "ctrl.custInfo[d.value]" >
		</div>
	</div>
	<div align="right" style="margin: 0.5em;padding-right: 2em;">
		<button class="btn btn-primary btn-sm" type="button" ng-show="ctrl.editMode" ng-click="ctrl.querySubscribersData()" ng-disabled="!ctrl.custInfo.idTaxid">帶入資料</button>
		<button class="btn btn-primary btn-sm" type="button" ng-show="ctrl.editMode" ng-click="ctrl.update()" ng-disabled="!ctrl.custInfo||!ctrl.custInfo.serviceId">送出</button>
		<button ng-show="ctrl.role!='noc'&& !ctrl.editMode " class="btn btn-primary btn-sm" type="button" ng-click="ctrl.editMode=true">啟用編輯模式</button>
		<button class="btn btn-primary btn-sm" type="button" ng-show="ctrl.editMode" ng-click="ctrl.editMode=false">關閉編輯模式</button>
	</div>
	<!-- 多筆清單 -->
	<modal mid="userListModal" title="UserList" width="100%" show-close="true" show-yes="false" show-no="false"  >
		<table class="table table-hover " style="font-size: 0.8em;">
			<tr>
				<th ng-repeat="h in ctrl.userListColumn">{{h.label}}</th>
			</tr>
			<tr ng-repeat="u in ctrl.custList" ng-click="ctrl.custSelected(u)" data-dismiss="modal">
				<td ng-repeat="h in ctrl.userListColumn">{{u[h.value]}}</td>
		  	</tr>
		</table>
	</modal>
	<modal  mid="dateModal" title="DatePicker" width="50%" show-close="true" show-yes="true" show-no="true"  yes-function="ctrl.downloadExcel()">
		<div>
			<div class="date-label" style="display: block;">Date:</div>
		    <input class="Mdate date-label" style="display: inline;text-align: center;" ng-model="ctrl.dateS" type="text" >
		    <div class="date-label" style="display: block;">到</div>
		    <input class="Mdate date-label" style="display: inline;text-align: center;" ng-model="ctrl.dateE" type="text" >
		</div>
	</modal>
		
	<!-- Detail頁面 -->
	<div id="pageMenu" class="dropdown" style="margin-bottom: 1em;"  >
		<button class="btn btn-secondary dropdown-toggle"  style="width: 30em;height: 1.5em;line-height: 0;" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ng-mouseover="ctrl.mouseover(this)">
			{{ctrl.selectedPage!=null?ctrl.pages[ctrl.selectedPage].title:'Dropdown Select' }}
		</button>
		<div class="dropdown-menu" aria-labelledby="dropdownMenuButton" style="width: 30em;text-align: center;">
			<a class="dropdown-item {{ctrl.selectedPage == $index?'active':''}}" href="#" ng-repeat="p in ctrl.pages" ng-click="ctrl.selectedPage = $index" style="height: 1.5em;line-height: 0.5em;">{{p.title}}</a>
		</div>
	</div>
	<div ng-repeat="p in ctrl.pages" class="tabContent" ng-show="ctrl.selectedPage==={{$index}}">
		<div ng-include="p.url" ></div>  
	</div>
</div>


</body>
<%@ include file="footer.jsp" %>

</html>