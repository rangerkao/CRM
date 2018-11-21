angular.module('MainApp')
	.controller('ChangeRecordCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryChangeRecord',function(event,data){			
			self.custInfo = data.cust;
			//self.query('query');
		});
		
		$scope.$on('reset',function(event,data){	
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.buttonControl = false;
			self.cardData = {};
			self.numberData = {};
		})();
		
		self.cardHeader = [
			{title:'新 IMIS',value:'newValue',size:'12em'},
			{title:'舊 IMSI',value:'oldValue',size:'12em'},
			{title:'類型',value:'orderType',size:'12em'},
			{title:'時間',value:'time',size:'12em'},
			];
		
		self.buttonControl=false;
		
		self.queryCardRecord = function(){
			self.buttonControl=true;
			self.cardData = {};
			var input = {
					serviceid:self.custInfo.serviceId
					}
			AjaxService.query('queryCardChangeRecord',input)
				.then(function successCallback(response) {
					var data = response.data
					if(data['error']){
						alert(data['error']);
					}else{
						self.cardData=data['data'];
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					self.buttonControl=false;
			    });
		}
		
		self.numberHeader = [
			{title:'新 Msisdn',value:'newValue',size:'12em'},
			{title:'舊 Msisdn',value:'oldValue',size:'12em'},
			{title:'時間',value:'time',size:'12em'},
			];
		
		self.queryNumberRecord = function(){
			self.buttonControl=true;
			self.numberData = {};
			var input = {
					serviceid:self.custInfo.serviceId,
					}
			AjaxService.query('queryNumberChangeRecord',input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						self.numberData = data['data'];
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					self.buttonControl=false;
			    });
		}
		
	}]);