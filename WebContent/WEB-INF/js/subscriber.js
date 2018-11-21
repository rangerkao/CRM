angular.module('MainApp')
	.controller('SubscriberCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self=this;

		self.conditions = [
			//{name:"護照ID",value:"psid"},
			{name:"香港號",value:"s2tm"},
			{name:"台灣號",value:"home"},
			{name:"VLN",value:"vln"},
			{name:"IMSI",value:"imsi"},
			{name:"ID/TAX ID",value:"id"},
			{name:"名稱",value:"name"},
			];
		
		self.selectedCondition = 1;
		
		
		var dataSizeL = "col-sm-6";
		var dataSizeS = "col-sm-3";
		self.userDataColumn = [
			{label:"S2T MSISDN",value:"s2tMsisdn",size:dataSizeS,editable:false},
			{label:"Home　MSISDN",value:"chtMsisdn",size:dataSizeS,editable:false},
			{label:"Service ID",value:"serviceId",size:dataSizeS,editable:false},
			{label:"狀態",value:"status",size:dataSizeS,editable:false},
			{label:"代辦處代號",value:"agency",size:dataSizeS,editable:true},
			{label:"姓名",value:"name",size:dataSizeS,editable:true},
			{label:"統一編號/證號",value:"idTaxid",size:dataSizeL,editable:true},
			{label:"負責人",value:"chair",size:dataSizeS,editable:true},
			{label:"負責人ID",value:"chairId",size:dataSizeS,editable:true},
			{label:"聯絡電話",value:"phone",size:dataSizeS,editable:true},
			{label:"生日(民國)",value:"birthday",size:dataSizeS,editable:true},
			{label:"戶籍地址",value:"permanentAddress",size:dataSizeL,editable:true},
			{label:"帳單地址",value:"billingAddress",size:dataSizeL,editable:true},
			{label:"Email",value:"email",size:dataSizeS,editable:true},
			{label:"類型",value:"type",size:dataSizeS,editable:true,type:'button'},
			{label:"護照姓名",value:"passportName",size:dataSizeS,editable:true},
			{label:"護照ID	",value:"passportId",size:dataSizeS,editable:true},
		];
		
		self.userListColumn = [
			{label:"S2T MSISDN",value:"s2tMsisdn",size:dataSizeS},
			{label:"Home　MSISDN",value:"chtMsisdn",size:dataSizeS},
			{label:"Service ID",value:"serviceId",size:dataSizeS},
			{label:"狀態",value:"status",size:dataSizeS},
			{label:"姓名",value:"name",size:dataSizeS},
			{label:"統一編號/證號",value:"idTaxid",size:dataSizeL},
			{label:"啟用時間",value:"activatedDate",size:dataSizeL},
			{label:"退租時間",value:"canceledDate",size:dataSizeL},
		];
		
		self.custList = [];
		
		self.custSelected = function(cust){
			self.custInfo = cust;
			self.queryDetail();
			
		}
		
//查詢***************************************
		self.queryList=function(){
			
			if(!self.searchValue)return;
			
			$scope.$broadcast('reset',{});
			self.custInfo = [];
			self.custList = [];
			
			ActionService.block();
			var input = {type:self.conditions[self.selectedCondition].value,input:self.searchValue};
			
			AjaxService.query('querySubscriber', input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
						ActionService.unblock();
					}else{
						if(data['data'].length==1){
							self.custSelected(data['data'][0]);
						}else if(data['data'].length>1){
							self.custList = data['data'];
							$('#userListModal').modal('handleUpdate');
							$('#userListModal').modal('show');
						}else{
							
						}
				    	ActionService.unblock();
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					ActionService.unblock();
			    });
		};
		
		self.onSelectedTypeKeyDown = function(event){
			if(event.keyCode ==13){
				self.queryList();
			}
		}
		
		
		//20181102，帶入公司資料
		self.querySubscribersData=function(){
			ActionService.block();
			var input = {idTaxid:self.custInfo.idTaxid};
			
			AjaxService.query('querySubscribersData', input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							//angular.forEach(obj, iterator, [context]);
							angular.forEach(self.userDataColumn, function(value, key) {
								if(value.editable && (!self.custInfo[value.value]||!self.custInfo[value.value]!='')){
									self.custInfo[value.value] = data['data'][value.value];
								}
							});
							self.custInfo['seq'] = data['data']['seq'];
						}else{
							alert("查無結果");
						}
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					ActionService.unblock();
			    });
		};
		
//update
		self.update=function(){
			ActionService.block();
			/*input={
					name:self.custInfo.name,
					birthday:self.custInfo.name,
				idTaxid:self.custInfo.name,
				phone:self.custInfo.name,
				email:self.custInfo.name,
				permanentAddress:self.custInfo.name,
				billingAddress:self.custInfo.name,
				agency:self.custInfo.name,
				remark :self.custInfo.name,
				type:self.custInfo.name,
					};*/
			var input = angular.copy(self.custInfo);
			
			delete input.pricePlanId;
			AjaxService.query('addOrUpdate',input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else if(data['data']){
						self.custInfo = data['data'];
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					ActionService.unblock();
			    });
		};
		
//子分頁資料
		self.selectedPage = 0;
		self.pages=[
	           {url:'pages/detail',title:'供裝資訊',content:'供裝資訊',active:false,disabled:false},
	           {url:'pages/qos',title:'CMHK QoS查詢',content:'CMHK QoS查詢',active:false,disabled:false},
	           {url:'pages/nameVerified',title:'實名制登記',content:'實名制登記',active:false,disabled:false},
	           {url:'pages/sms',title:'系統簡訊(開通、落地、超量)',content:'系統簡訊(開通、落地、超量)',active:false,disabled:false},
	           //{url:'jsp/pages/application',title:'申請書回收查詢',content:'申請書回收查詢',active:true,disabled:false},
	           {url:'pages/currentMonth',title:'數據用量月累計',content:'數據用量月累計',active:false,disabled:false},
	           {url:'pages/currentDay',title:'數據用量日累計',content:'數據用量日累計',active:false,disabled:false},
	           //{url:'jsp/pages/dataRate',title:'各國費率表',content:'各國費率表',active:false,disabled:false},
	           //{url:'jsp/pages/HKChinaCompare',title:'香港與中國門號對應表',content:'香港與中國門號對應表',active:false,disabled:false},
	           //{url:'jsp/pages/queryNameVarified',title:'實名制登記查詢',content:'實名制登記查詢',active:false,disabled:false}
	           {url:'pages/dataOpenSMS',title:'數據開通簡訊發送',content:'數據開通簡訊發送',active:false,disabled:false},
	           {url:'pages/vip',title:'VIP 設定',content:'VIP 設定',active:false,disabled:false},
	           {url:'pages/tapout',title:'Tapout 查詢',content:'Tapout 查詢',active:false,disabled:false},
	           {url:'pages/changeRecord',title:'異動記錄查詢',content:'異動記錄查詢',active:false,disabled:false},
	           ];
		
		self.queryDetail = function(){
			$scope.$broadcast('queryDetail',{cust:self.custInfo});
			$scope.$broadcast('queryQos',{cust:self.custInfo});
			$scope.$broadcast('querySMS',{cust:self.custInfo});
			$scope.$broadcast('queryMonth',{cust:self.custInfo});
			$scope.$broadcast('queryDay',{cust:self.custInfo});
			$scope.$broadcast('queryNameVerified',{cust:self.custInfo});
			$scope.$broadcast('queryDataOpenSMS',{cust:self.custInfo});
			$scope.$broadcast('queryVIP',{cust:self.custInfo});
			$scope.$broadcast('queryTapout',{cust:self.custInfo});
			$scope.$broadcast('queryChangeRecord',{cust:self.custInfo});
		};
		
		self.mouseover = function(){
			//$('#dropdownMenuButton').click();
		}

		
//Excel下載
		//以JSP->input->angular
		self.role = $("#userRole").val();
		self.downloadExcel = function(){
			ActionService.block();
			var url = "downloadSubscriberExcel?" +
					"dateS="+(self.dateS?self.dateS.replace(/\//g,''):"")+"&"+
					"dateE="+(self.dateE?self.dateE.replace(/\//g,''):"");
			
	        location.href = url;
	        window.setTimeout(function(){ 
	        	ActionService.unblock();
	        	},5000); 
		}
		
		
	}]);