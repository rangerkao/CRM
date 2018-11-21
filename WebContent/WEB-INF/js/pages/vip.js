angular.module('MainApp')
	.controller('VIPCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryVIP',function(event,data){			
			self.custInfo = data.cust;
			self.query('query');
			self.msisdn = self.custInfo.s2tMsisdn;
		});
		
		$scope.$on('reset',function(event,data){	
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.buttonControl=true;
			self.data = {};
			self.msisdn = null;

		})();
		
		self.header = [
			{title:'建立時間',value:'createtime',size:'50%'},
			{title:'取消時間',value:'canceledtime',size:'50%'},
			//{title:'備註',value:'remark',size:'30em'},
			];
		
		self.query = function(action){
			self.buttonControl=true;
			self.data = {};
			var input = {
					serviceid:self.custInfo.serviceId
					}
			AjaxService.query(action+'VIP',input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						self.data=data['data'];
						if(action == 'add')
							$('#vipSMSCheck').modal('show');
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					self.buttonControl=false;
			    });
		}
		
		self.sendSMS = function(){
			self.buttonControl=true;
			var input = {
					serviceid:self.custInfo.serviceId,
					msisdn:self.msisdn,
					msg:self.data.msg
					}
			AjaxService.query('sendVIP',input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						alert(data['data']);
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					self.buttonControl=false;
			    });
		}
		
		//true為鎖定取消功能
		self.verify = function(){
			if(self.data.list && self.data.list.length!=0 && self.data.list[0].canceledtime.trim() == '')
				return false;
			return true;
		}
		
	}]);