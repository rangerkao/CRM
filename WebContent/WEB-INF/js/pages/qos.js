angular.module('MainApp')
	.controller('QosCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryQos',function(event,data){			
			self.custInfo = data.cust;
			self.query();
		});
		
		$scope.$on('reset',function(event,data){
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.dataList = [];
			self.tableData = [];
			
			self.onePageNumber = 10;
			self.nowPage = 1;
			self.lastPage = 0;
		})();
		
		self.header = [
			{title:'IMSI',value:'imsi',size:'15%'},
			{title:'Msisdn',value:'msisdn',size:'15%'},
			{title:'Action',value:'action',size:'15%'},
			{title:'Plan',value:'plan',size:'10%'},
			{title:'連線結果',value:'returnCode',size:'15%'},
			{title:'供裝結果',value:'resultCode',size:'15%'},
			{title:'供裝時間',value:'createTime',size:'15%'}
			];
		self.query = function(){
			self.tableData = [];
			var input = {
					imsi:self.custInfo.s2tIMSI,
					msisdn:self.custInfo.s2tMsisdn,
					activatedDate:self.custInfo.activatedDate,
					canceledDate:self.custInfo.canceledDate
					}
			AjaxService.query('queryQos',input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						self.dataList=data['data'];
						self.pagging(1);
						self.lastPage = Math.ceil(self.dataList.length/self.onePageNumber);
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
			    });
		}
		
		//分頁
		
		self.pagging = function(page){
			self.nowPage = page;
			self.tableData = [];
			angular.forEach(self.dataList,function(value,index){
				var lowBase = (self.nowPage-1)*self.onePageNumber;
				var highBase = self.nowPage*self.onePageNumber;
				if(index>=lowBase && index< highBase){
					self.tableData.push(value);
				}
			});
		};
	}]);