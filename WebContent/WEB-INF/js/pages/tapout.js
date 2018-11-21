angular.module('MainApp')
	.controller('TapoutCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryTapout',function(event,data){			
			self.custInfo = data.cust;
			//self.query();
		});
		
		$scope.$on('reset',function(event,data){	
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.selectedType = 3;
			self.buttonControl=false;
			self.dataList = [];
			self.tableData=[];
			
			self.onePageNumber = 10;
			self.nowPage = 1;
			self.lastPage = 0;
		})();
		
		self.header = [
			{title:"始話日期",value:"startDate",size:"5em"},
		    {title:"漫遊網",value:"location",size:"4em"},
		    {title:"發話號碼/收話號碼",value:"phonenumber",size:"10em"},
		    {title:"通話種類",value:"type",size:"5em"},
		    {title:"始話時刻",value:"startTime",size:"5em"},
		    {title:"終話時刻",value:"endTime",size:"5em"},
		    {title:"使用量(秒/則/Bytes)",value:"unit",size:"10em"},
		    {title:"漫遊費用",value:"amount",size:"5em"},
		    {title:"原始費用",value:"totalCharge",size:"5em"},
		    {title:"優惠費用",value:"discountCharge",size:"5em"},
		    {title:"結果費用",value:"finalCharge",size:"5em"}
		    ];
		
		self.types = [
			{label:"通話",value:"voice"},
		    {label:"簡訊",value:"sms"},
		    {label:"數據",value:"data"},
		    {label:"全部",value:"all"}
		];
		    
		
		
		self.query = function(){
			self.buttonControl=true;
			self.tableData=[];
			var input = {
					serviceid:self.custInfo.serviceId,
					type:self.types[self.selectedType].value,
					from:self.startDate?self.startDate:'',
					to:self.endDate?self.endDate:'',
					}
			AjaxService.query('queryTapoutData',input)
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
					self.buttonControl=false;
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
		
		//Excel下載
		//以JSP->input->angular
		self.role = $("#userRole").val();
		self.downloadExcel = function(){
			ActionService.block();
			var url = "downloadTapOutrExcel?" +
					"serviceid="+self.custInfo.serviceId+"&"+
					"type="+self.types[self.selectedType].value+"&"+
					"from="+(self.startDate?self.startDate:'')+"&"+
					"to="+(self.endDate?self.endDate:'');
			
	        location.href = url;
	        window.setTimeout(function(){ 
	        	ActionService.unblock();
	        	},5000); 
		}
		
	}]);