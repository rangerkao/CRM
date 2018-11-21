<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="detail" ng-controller="NameVerifiedCtrl as sctrl" style="position: relative;top: -1.5em;">
	<table class="dataTable" style="width: 90%;display: inline-table;margin-bottom: 1em; ">
		<tr>
			<td>中國</td>
			<td colspan="6"><font color="red" size="6em">{{sctrl.china.errorMsg}}</font></td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader1">{{h.title}}</td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader1" ng-switch="h.type" style="width: {{h.size}};">
				<select ng-switch-when="selector" ng-model="sctrl.china[h.value]" ng-change="sctrl.onSelectChange(sctrl.china)">
					<option ng-repeat="i in sctrl.identityTypes" value="{{i.value}}">{{i.label}}</option>
				</select>
				<input ng-switch-when="input" ng-model="sctrl.china[h.value]">
				<label ng-switch-when="label" >{{sctrl.china[h.value]}}</label>
				<div ng-switch-default></div>
			</td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader2" colspan="{{h.colspan?h.colspan:'1'}}">{{h.title}}</td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader2" ng-switch="h.type" style="width: {{h.size}};" colspan="{{h.colspan?h.colspan:'1'}}">
				<select ng-switch-when="selector" ng-model="sctrl.china[h.value]">
					<option ng-repeat="i in sctrl.identityTypes" value="{{i.value}}">{{i.label}}</option>
				</select>
				<input ng-switch-when="input" ng-model="sctrl.china[h.value]" style="width: 98%;">
				<label ng-switch-when="label" >{{sctrl.china[h.value]}}</label>
				<div ng-switch-default style="width: 100%;text-align: right;">
					<button class="btn btn-sm btn-primary" ng-click="sctrl.verify(sctrl.china,sctrl.setChina)"
					ng-disabled="sctrl.buttonControl || sctrl.commonValidate(sctrl.china) || sctrl.validateIdFormate(sctrl.china) || sctrl.validateComfirm(sctrl.china,sctrl.chinaOld)">認證</button>
					<button class="btn btn-sm btn-success" ng-click="sctrl.update(sctrl.china,sctrl.setChina)"
					ng-disabled="sctrl.buttonControl || sctrl.commonValidate(sctrl.china) || sctrl.validateIdFormate(sctrl.china) || !sctrl.validateComfirm(sctrl.china,sctrl.chinaOld)">更新</button>
				</div>
			</td>
		</tr>
		<tr ng-show="sctrl.china.history" style="background-color: black;color:white;text-align: center; font-weight: 900;"> <td colspan="10">History</td></tr>
		<tr ng-repeat-start="item in sctrl.china.history">
			<td ng-repeat="h in sctrl.dataHeader1" style="width: {{h.size}};">{{item[h.value]}}</td>
		</tr>
		<tr ng-repeat-end="item in sctrl.china.history" style="border-bottom:1px solid black; ">
			<td ng-repeat="h in sctrl.dataHeader2" style="width: {{h.size}};" colspan="{{h.colspan?h.colspan:'1'}}">{{item[h.value]}}</td>
		</tr>
	</table>
	
<!-- 新加坡  -->
	<table class="dataTable" style="width: 90%;display: inline-table; margin-bottom: 1em;" ng-show="sctrl.sgp">
		<tr>
			<td>新加坡</td>
			<td colspan="6"><font color="red">{{sctrl.sgp.errorMsg}}</font></td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader1">{{h.title}}</td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader1" ng-switch="h.type" style="width: {{h.size}};">
				<select ng-switch-when="selector" ng-model="sctrl.sgp[h.value]" ng-change="sctrl.onSelectChange(sctrl.sgp)">
					<option value="Passport">Passport</option>
				</select>
				<input ng-switch-when="input" ng-model="sctrl.sgp[h.value]">
				<label ng-switch-when="label" >{{sctrl.sgp[h.value]}}</label>
				<div ng-switch-default></div>
			</td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader2" colspan="{{h.colspan?h.colspan:'1'}}">{{h.title}}</td>
		</tr>
		<tr>
			<td ng-repeat="h in sctrl.dataHeader2" ng-switch="h.type" style="width: {{h.size}};" colspan="{{h.colspan?h.colspan:'1'}}">
				<select ng-switch-when="selector" ng-model="sctrl.sgp[h.value]">
					<option ng-repeat="i in sctrl.identityTypes" value="{{i.value}}">{{i.label}}</option>
				</select>
				<input ng-switch-when="input" ng-model="sctrl.sgp[h.value]" style="width: 98%;">
				<label ng-switch-when="label" >{{sctrl.sgp[h.value]}}</label>
				<div ng-switch-default style="width: 100%;text-align: right;">
<!-- 						<button class="btn btn-sm btn-primary" 
						ng-disabled="sctrl.buttonControl || sctrl.commonValidate(sctrl.china) || sctrl.validateIdFormate(sctrl.china) || sctrl.validateComfirm(sctrl.china,sctrl.chinaOld)">認證</button>
						<button class="btn btn-sm btn-success" 
						ng-disabled="sctrl.buttonControl || sctrl.commonValidate(sctrl.china) || sctrl.validateIdFormate(sctrl.china) || !sctrl.validateComfirm(sctrl.china,sctrl.chinaOld)">更新</button>
-->					</div>
			</td>
		</tr>
		<!-- <tr ng-show="sctrl.china.history" style="background-color: black;color:white;text-align: center; font-weight: 900;"> <td colspan="10">History</td></tr>
		<tr ng-repeat-start="item in sctrl.china.history">
			<td ng-repeat="h in sctrl.dataHeader1" style="width: {{h.size}};">{{item[h.value]}}</td>
		</tr>
		<tr ng-repeat-end="item in sctrl.china.history" style="border-bottom:1px solid black; ">
			<td ng-repeat="h in sctrl.dataHeader2" style="width: {{h.size}};" colspan="{{h.colspan?h.colspan:'1'}}">{{item[h.value]}}</td>
		</tr> -->
	</table>
	
<!-- 全域查詢 -->
	<div style="display: inline-block;width: 90%;background-color: yellow;margin-top: 1em;height: 1.8em;font-size: 0.8em;line-height: 1.5em;font-weight: bold;"><font color="red">＊此查詢內容為資料庫所有客戶認證資訊</font></div>
	<div style="width: 90%; display: inline-block;">
		<input type="button" style="width: 7em;" value="門號對應表" class="btn btn-sm btn-info" data-toggle="modal" data-target="#compareModal">
		<div class="btn-group" role="group">
			<button ng-repeat="c in sctrl.conditions" type="button" class="btn btn-sm {{sctrl.selectedCondition==$index?'btn-primary':'btn-secondary'}}" ng-click="sctrl.selectedCondition=$index" style="border-radius: 5px;">{{c.name}}</button>
		</div>
		<div class="input-group" style="display:inline-flex;width: 25em;top:+5px;margin-bottom: 10px;">
			<input ng-model="sctrl.searchValue" type="text" class="form-control" placeholder=""  ng-keydown="sctrl.onSearchKeyDown($event)"
					style="font-size: 20px;font-weight: bold;height: 1.7em;margin-right: 0;margin-left: 3em;" required>
			<div class="input-group-append">
				<button class="btn btn-primary btn-sm" ng-click="sctrl.search()" ng-disabled="sctrl.selectedCondition==null || !sctrl.searchValue || sctrl.searching ">Search</button>
				{{(sctrl.searching?'查詢中...':'')}}
			</div>
		</div>	
	</div>
	
	<table class="dataTable table table-sm" style="width: 90%;margin: 0 5% 0 5%; margin-bottom: 1em;">
		<thead>
			<tr class="table-active">
		      <th scope="col" ng-repeat="h in sctrl.searchHeader" style="width:{{h.size}}">{{h.title}}</th>
		    </tr>
		</thead>
		<tbody>
		    <tr ng-repeat="d in sctrl.nameVerifiedList">
		      <td ng-repeat="h in sctrl.searchHeader" >{{d[h.value]}}</td>
		    </tr>
		</tbody>
	</table>
	<modal mid="compareModal" title="compare" width="50%" show-close="true" show-yes="false" show-no="false" >
		<table>
			<tr><th>HK (CMHK) MSISDN</th><th>China (CMCC) MSISDN</th></tr>
			<tr ng-repeat="d in sctrl.msisdnCompare "><td>{{d.s2tMsisdn}}</td><td>{{d.chn}}</td></tr>
		</table>
	</modal>
</div>