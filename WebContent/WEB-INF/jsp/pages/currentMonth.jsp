<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="currentMonth" ng-controller="MonthCtrl as sctrl">
	<div class="container-fluid row" style="top: -1em;position: relative;height: 1.5em;font-size: 0.8em;color: red;">
		<div class="col-sm-9" style="position: rela tive; top: 0.5em;">
			<div class="date-label">Date:</div>
		    <input ng-model="sctrl.startDate" type="text" class="Mdate">
		    <div class="date-label">to</div>
		    <input ng-model="sctrl.endDate" type="text" class="Mdate">
		    <button type="button" class="btn btn-secondary btn-sm" style="height: 1.7em;width: 3em;padding: 0;position: relative;top: -0.2em;" ng-click="sctrl.query()" ng-disabled="sctrl.custInfo==null||sctrl.buttonControl">Go</button>
			{{(sctrl.custInfo && sctrl.buttonControl?'查詢中...':'')}}
		</div>
		<div class="col-sm-3" style="color:black;">
			<button class="btn btn-secondary btn-sm" style="height: 2em;width: 3em;padding: 0;" ng-click="sctrl.pagging(sctrl.nowPage-1)" ng-disabled="sctrl.nowPage==1"><i class="material-icons">arrow_back</i></button>
			<label style="height: 2em;width: 6em;">{{sctrl.nowPage}}/{{sctrl.lastPage}}</label>
			<button class="btn btn-secondary btn-sm" style="height: 2em;width: 3em;padding: 0;" ng-click="sctrl.pagging(sctrl.nowPage+1)" ng-disabled="sctrl.nowPage>=sctrl.lastPage"><i class="material-icons">arrow_forward</i></button>
		</div>
	</div>
	<table class="dataTable table table-sm" style="text-align:  center; width: 98%;margin: 0 1% 0 1%;    line-height: 1em;">
	  <thead>
	    <tr class="table-active">
	      <th scope="col" ng-repeat="h in sctrl.header" style="width: {{h.size}};">{{h.title}}</th>
	    </tr>
	  </thead>
	  <tbody>
	    <tr ng-repeat="d in sctrl.tableData">
	      <td ng-repeat="h in sctrl.header" >{{d[h.value]}}</td>
	    </tr>
	  </tbody>
	</table>
</div>
<script>
$(function(){
	$('.Mdate').datepicker({
		format: "yyyy/mm",
	    autoclose: true,
	    startView: 1,
	    minViewMode: 1,
	    language: "zh-TW",
	    clearBtn: true,
	});
});
</script>
