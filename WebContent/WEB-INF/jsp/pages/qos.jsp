<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="detail" ng-controller="QosCtrl as sctrl">
	<div class="container-fluid row" style="top: -1em;position: relative;height: 3em;font-size: 0.8em;color: red;">
		<div class="col-sm-9" style="position: rela tive; top: 0.5em;">
			方案 1:一般用戶(失效方案)。2/9:一般用戶 。3/5:華人上網包(香港)。4/6:高量／華人上網包(香港+大陸)。7:Joy、GO2PLAY。10:多國上網包
		</div>
		<div class="col-sm-3" style="color:black;">
			<div>
				<button class="btn btn-secondary btn-sm" style="height: 2em;width: 3em;padding: 0;" ng-click="sctrl.pagging(sctrl.nowPage-1)" ng-disabled="sctrl.nowPage==1"><i class="material-icons">arrow_back</i></button>
				<label style="height: 2em;width: 6em;">{{sctrl.nowPage}}/{{sctrl.lastPage}}</label>
				<button class="btn btn-secondary btn-sm" style="height: 2em;width: 3em;padding: 0;" ng-click="sctrl.pagging(sctrl.nowPage+1)" ng-disabled="sctrl.nowPage>=sctrl.lastPage"><i class="material-icons">arrow_forward</i></button>
			</div>
		</div>
	</div>
	<table class="dataTable table table-sm" style="text-align:  center; width: 98%;margin: 0 1% 0 1%;    line-height: 1em;">
	  <thead>
	    <tr class="table-active">
	      <th scope="col" ng-repeat="h in sctrl.header" width="{{h.size}}">{{h.title}}</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr ng-repeat="d in sctrl.tableData">
	      <td ng-repeat="h in sctrl.header" >{{d[h.value]}}</td>
	    </tr>
	  </tbody>
	</table>
</div>