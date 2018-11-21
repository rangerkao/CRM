angular.module('MainApp')
	.controller('MonthCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryMonth',function(event,data){			
			self.custInfo = data.cust;
			//self.query();
		});
		
		$scope.$on('reset',function(event,data){
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.dataList = [];
			self.buttonControl = false;
			self.tableData=[];
			
			self.onePageNumber = 10;
			self.nowPage = 1;
			self.lastPage = 0;
		})();
		
		self.header = [
			{title:'統計月份',value:'month',size:'5em'},
			{title:'累計費用',value:'charge',size:'5em'},
			{title:'最後檔案',value:'lastFileId',size:'5em'},
			{title:'發送簡訊次數',value:'smsTimes',size:'7em'},
			{title:'最後使用時間',value:'lastDataTime',size:'7em'},
			{title:'累計流量(byte)',value:'volume',size:''},
			{title:'建立時間',value:'createDate',size:'5em'},
			{title:'中斷數據',value:'everSuspend',size:'5em'},
			{title:'最後警示額度',value:'lastAlertThreshold',size:'7em'}
			];

		self.query=function(){
			self.tableData = [];
			var input = {
					serviceid:self.custInfo.serviceId,
					startDate:(self.startDate?self.startDate.replace("/",""):''),
					endDate:(self.endDate?self.endDate.replace("/",""):'')	
			}
			self.buttonControl = true;
			AjaxService.query('queryCurrentMonth', input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							self.dataList = data['data'];
							self.pagging(1);
							self.lastPage = Math.ceil(self.dataList.length/self.onePageNumber);
						}
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					self.buttonControl = false;
			    });
		};
		
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