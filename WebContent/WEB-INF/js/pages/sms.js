angular.module('MainApp')
	.controller('SMSCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('querySMS',function(event,data){			
			self.custInfo = data.cust;
			//self.query();
		});
		
		$scope.$on('reset',function(event,data){	
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.buttonControl = false;
			self.dataList = [];
			self.tableData=[];
			
			self.onePageNumber = 10;
			self.nowPage = 1;
			self.lastPage = 0;
		})();
		

		self.header = [
			{title:'簡訊類型',value:'smsclass',size:'10%'},
			{title:'發送號碼',value:'phoneno',size:'10%'},
			{title:'發送時間',value:'sendTime',size:'10%'},
			{title:'簡訊內容',value:'content',size:'70%'}
			];

		self.query=function(){
			self.tableData=[];
			var input = {
					chtMsisdn:self.custInfo.chtMsisdn,
					s2tMsisdn:self.custInfo.s2tMsisdn, 
					startDate:(self.startDate?self.startDate:''), 
					endDate:(self.endDate?self.endDate:''), 
					activatedDate:self.custInfo.activatedDate, 
					canceledDate:self.custInfo.canceledDate};
			
			self.buttonControl = true;
			AjaxService.query('querySMS', input)
				.then(function successCallback(response) {
					var data = response.data
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							self.dataList = data['data'];
							self.pagging(1);
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
			self.lastPage = Math.ceil(self.dataList.length/self.onePageNumber);
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

