angular.module('MainApp')
	.controller('DayCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryDay',function(event,data){			
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
			{title:'累計日期',value:'day',size:'5em'},
			{title:'國家業者',value:'mccmnc',size:'5em'},
			{title:'累計費用',value:'charge',size:'5em'},
			{title:'累計流量(byte)',value:'volume',size:''},
			{title:'每日警示',value:'alert',size:'7em'},
			{title:'最後檔案',value:'lastFileId',size:'5em'},
			{title:'最後使用時間',value:'lastDataTime',size:'7em'},
			{title:'建立時間',value:'createDate',size:'5em'},
			];

		self.query=function(){
			self.tableData = [];
			var input = {
					serviceid:self.custInfo.serviceId,
					startDate:(self.startDate?self.startDate.replace(/\//g,""):''),
					endDate:(self.endDate?self.endDate.replace(/\//g,""):'')	
					//以正規表示式   /取代值/g，g表global取代，實現replaceAll，/為特殊字元需要以\跳脫
			}
			self.buttonControl = true;
			AjaxService.query('queryCurrentDay', input)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data']){
							console.log(data['data']);
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