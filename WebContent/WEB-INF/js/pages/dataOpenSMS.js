angular.module('MainApp')
	.controller('DataOpenSMSCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryDataOpenSMS',function(event,data){			
			self.custInfo = data.cust;
			self.query();
			self.msisdn = self.custInfo.s2tMsisdn
		});
		
		$scope.$on('reset',function(event,data){	
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.selecteType='android';
			self.msisdn = null;			
		})();
		
		
		
		self.query=function(){
			self.smsContent = {};
			AjaxService.query('queryDataOpenSMS', {serviceid:self.custInfo.serviceId})
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							self.smsContent = data['data'];
						}
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
			    });
		};
		
		self.send=function(){
			//','造成json分段問題，以{dot}取代，後端再轉回
			var input = {
					msisdn:self.msisdn,
					msgs:[
						self.smsContent.typeA.replace(/,/g,'{dot}'),
						self.smsContent.typeB.replace(/,/g,'{dot}'),
						(self.selecteType=='android'?self.smsContent.typeC.android:self.smsContent.typeC.iphone).replace(/,/g,'{dot}')]
			}

			AjaxService.query('sendDataOpenSMS', input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							alert(data['data']);
						}
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
			    });
		};
		
		
	}]);