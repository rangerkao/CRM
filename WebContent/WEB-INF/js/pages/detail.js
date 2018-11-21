angular.module('MainApp')
	.controller('DetailCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryDetail',function(event,data){			
			self.custInfo = data.cust;
			self.query();	
		});
		
		$scope.$on('reset',function(event,data){	
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.detail=[];
			self.CHNA='';
		})();
		
		self.query=function(){
			self.detail=[];
			self.CHNA = '(查詢中)';
			AjaxService.query('queryDetail', {serviceid:self.custInfo.serviceId})
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							self.detail=data['data'];
							//處理vln
							var vln = '';
							angular.forEach(self.custInfo.vlns,function(value,key){
								if(vln!='') vln+=',';
								vln+=value;
							});
							self.detail.vln = vln;
						}
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					self.queryCHNA();
			    });
		};
		
	
		self.queryCHNA=function(){
			AjaxService.query('queryWhetherAppliedCHNA', {serviceid:self.custInfo.serviceId})
				.then(function successCallback(response) {
					var data = response.data
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							self.CHNA=data['data'];
						}else{
							self.CHNA='';
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