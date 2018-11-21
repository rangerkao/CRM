angular.module('MainApp')
	.controller('NameVerifiedCtrl',['AjaxService','ActionService','$scope',function(AjaxService,ActionService,$scope){
		var self = this;
		//外部觸發
		$scope.$on('queryNameVerified',function(event,data){	
			if(!data.cust) return;
			
			self.custInfo = data.cust;

			self.iniNameVerifiedData(self.china);
			self.query(self.china,self.setChina);
			
			angular.forEach(self.custInfo.vlns,function(value,key){
				var vln = value;
				//假如有新加坡號
				if(vln && vln.match(/^65/g)){
					self.sgp = {};
					self.sgp.vln = vln.substring(0,vln.indexOf("("));
					//不是環球卡時msisdn = s2t門號
					if(self.custInfo.pricePlanId.id == 139)
						self.sgp.msisdn = self.custInfo.chtMsisdn;
					else
						self.sgp.msisdn = self.custInfo.s2tMsisdn;
					
					self.sgp.type = 'Passport';
					self.sgp.id = self.custInfo.passportId;
					self.sgp.name = self.custInfo.passportName;
				}else if(vln && vln.match(/^86/g)){
				}
			});
			
		});
		
		$scope.$on('reset',function(event,data){	
			self.init();
		});
		
		//立即表示式初始化
		(self.init = function(){
			self.custInfo = null;
			self.buttonControl = true;
			self.china = {};
			self.sgp = null;
		})();

		self.dataHeader1 =[	
			{title:"證件類型",value:"type",size:"18em",type:"selector"},
			{title:"姓名",value:"name",size:"8em",type:"input"},	           
          	{title:"證號",value:"id",size:"8em",type:"input"},
          	{title:"中華/香港號",value:"msisdn",size:"",type:"label"},	           
          	{title:"當地副號",value:"vln",size:"10em",type:"label"},
          	{title:"建立時間",value:"createTime",size:"10em",type:"label"},
          	{title:"發送時間",value:"sendDate",size:"10em",type:"label"},
          	];
		
		self.dataHeader2 =[	
			{title:"備註",value:"remark",size:"",type:"input",colspan:4},
			{title:"狀態",value:"status",size:"",type:"label"},
			{title:"證件認證數",value:"usedCount",size:"",type:"label"},
			{title:"",value:"btn",size:"",type:""}
          	];		
		
		self.identityTypes = [
			{label:"台湾居民来往大陆通行证",value:"台湾居民来往大陆通行证"},
			{label:"Passport",value:"Passport"},
			{label:"港澳居民来往内地通行证",value:"港澳居民来往内地通行证"},
			{label:"中华人民共和国二代居民身份证",value:"中华人民共和国二代居民身份证"}
			];
		
		self.query = function(nameVerifiedData,callback){
			self.buttonControl = true;
			AjaxService.query('queryNameVarifiedData',nameVerifiedData)
				.then(function successCallback(response) {
					var data = response.data
					
					if(data['error']){
						alert(data['error']);
					}else{
						if(data['data'].current){
							nameVerifiedData = data['data'].current;
							if(nameVerifiedData.usedCount>=6)
								alert("此證件認證已超過5門！");
						}else{
							nameVerifiedData.type = '台湾居民来往大陆通行证';
						}
						nameVerifiedData.history = data['data'].history;
						callback(nameVerifiedData);
					}
				}, function errorCallback(response) {
					console.log('Error result:');
					console.log(response);
					alert('error');
				}).finally(function(){
					self.buttonControl = false;
			    });
		}
		
		self.msisdnCompare = [
			{s2tMsisdn:'85252215xxx',chn:'8618411045xxx'},
			{s2tMsisdn:'85252216xxx',chn:'8618411046xxx'},
			{s2tMsisdn:'85253940xxx',chn:'8618411040xxx'},
			{s2tMsisdn:'8525609xxxx',chn:'861841103xxxx'},
			{s2tMsisdn:'8526640xxxx',chn:'861391048xxxx'},
			{s2tMsisdn:'852676585xx',chn:'86184110485xx'},
			{s2tMsisdn:'852676586xx',chn:'86184110486xx'},
			{s2tMsisdn:'852676587xx',chn:'86184110487xx'},
			{s2tMsisdn:'852676588xx',chn:'86184110488xx'},
			{s2tMsisdn:'852676589xx',chn:'86184110489xx'},
			{s2tMsisdn:'852676590xx',chn:'86184110490xx'},
			{s2tMsisdn:'852676591xx',chn:'86184110491xx'},
			{s2tMsisdn:'852676592xx',chn:'86184110492xx'},
			{s2tMsisdn:'852676593xx',chn:'86184110493xx'},
			{s2tMsisdn:'852676594xx',chn:'86184110494xx'},
			{s2tMsisdn:'8526947xxxx',chn:'861391037xxxx'}
		];
		
		//<tr><th>HK (CMHK) MSISDN</th><th>China (CMCC) MSISDN</th></tr>
		
		self.iniNameVerifiedData = function(nameVerifiedData){
			var s2tNumber = self.custInfo.s2tMsisdn;
			var chinaMsisdn
			
			angular.forEach(self.msisdnCompare,function(value,index){
				//相當於s2tNumber.match(/^85252215.+/g)，i:ignoreCase、g:global、m:multiline
				var s2tString = value.s2tMsisdn;
				s2tString = s2tString.replace(/x/g,'');
				var chnString = value.chn;
				chnString = chnString.replace(/x/g,'');
				var regexp = new RegExp("^"+s2tString+".+",'g');
				if(s2tNumber.match(regexp)) chinaMsisdn = s2tNumber.replace(s2tString,chnString);
			});
			
			nameVerifiedData.vln = chinaMsisdn;
			
			//不是環球卡時msisdn = s2t門號
			if(self.custInfo.pricePlanId.id == 139)
				nameVerifiedData.msisdn = self.custInfo.chtMsisdn;
			else
				nameVerifiedData.msisdn = self.custInfo.s2tMsisdn;
		}
		
		self.commonValidate = function(nameVerifiedData){
			nameVerifiedData.errorMsg = '';
			var error = false;
			
			
			if(!self.custInfo){
				return true;
			}
			
			if((self.custInfo.canceledDate || self.custInfo.canceledDate != '' ) && self.custInfo.nowS2tActivated!="0"){
				error = true;
				nameVerifiedData.errorMsg += (nameVerifiedData.errorMsg ==''?'':',')+'此門號已被他人使用中';
			}
			
			
			if(!nameVerifiedData.msisdn){
				error = true;
				//nameVerifiedData.errorMsg += (nameVerifiedData.errorMsg ==''?'':',')+'無香港號';
			}else if(!nameVerifiedData.vln){
				error = true;
				nameVerifiedData.errorMsg += (nameVerifiedData.errorMsg ==''?'':',')+'無匹配中國號';
			}
			if(!nameVerifiedData.name){
				error = true;
				//nameVerifiedData.errorMsg += (nameVerifiedData.errorMsg ==''?'':',')+'請輸入姓名';
			}
			if(!nameVerifiedData.type){
				error = true;
				//nameVerifiedData.errorMsg += (nameVerifiedData.errorMsg ==''?'':',')+'請選擇證件類型';
			}
			if(!nameVerifiedData.id){
				error = true;
				//nameVerifiedData.errorMsg += (nameVerifiedData.errorMsg ==''?'':',')+'請輸入證號';
			}
			
			/*if((self.custInfo.canceledDate || self.custInfo.canceledDate != '' ) && self.custInfo.nowS2tActivated!="0"){
				self.china.errorMsg = '此門號已被他人使用中';
				//return ;
			}*/
			
			
			//return nameVerifiedData.errorMsg!='';
			return error;
		}
		
		self.validateIdFormate = function(nameVerifiedData){
			nameVerifiedData.errorMsg = '';
			//驗證證號格式
			var type = nameVerifiedData.type;
			var id = nameVerifiedData.id;
			
			if(
					//台胞證純數字狀態7位或8位，12位數字帶刮號內英文2碼
					(type=='台湾居民来往大陆通行证'&&!id.match(/^\w?\d{7,8}$/g) && !id.match(/^\w?\d{10}\(\w{1}\)$/g))||
					//
					(type=='Passport'&& false)||
					//港澳居民通行證，首數字H或M，後面為8或10位數字
					(type=='港澳居民来往内地通行证'&&!id.match(/^[H,M](\d{8}|\d{10})$/g))||
					//第二代身份證，//15或18位數字
					(type=='中华人民共和国二代居民身份证'&&!id.match(/^\d{15}|\d{18}$/g))
					)
				nameVerifiedData.errorMsg = '證件號格式錯誤';
			return nameVerifiedData.errorMsg!='';
		};
		
		self.validateComfirm = function(nameVerifiedData,old){
			//有歷史資料而且未認證(未認證視為只能更新)
			if(old.id){
				//未認證只能更新
				if(!old.sendDate || old.sendDate.trim() =='') return true;
				//證件類型 && 證件號碼 && 人相同，只能更新
				if(old.type==nameVerifiedData.type && old.id==nameVerifiedData.id && old.name==nameVerifiedData.name )	return true;
			}
		}
		
		//使用call function在異步查詢時設定到正確的參數
		self.setChina = function(nameVerifiedData){
			self.china = nameVerifiedData;
			self.chinaOld = {};
			angular.copy(self.china,self.chinaOld);
		}
		
		self.onSelectChange = function(nameVerifiedData){
			if(nameVerifiedData['type']=='Passport') 
				nameVerifiedData['id']= self.custInfo.passportId;
		}
		
		
//認證
		self.verify = function(nameVerifiedData,callback){
			nameVerifiedData.serviceid = self.custInfo.serviceId;
			self.buttonControl = true;
			AjaxService.query('addNameVerifiedData',nameVerifiedData)
			.then(function successCallback(response) {
				var data = response.data
				
				if(data['error']){
					alert(data['error']);
				}else{
					alert('認證完成');
				}
			}, function errorCallback(response) {
				console.log('Error result:');
				console.log(response);
				alert('error');
			}).finally(function(){
				self.buttonControl = false;
				self.query(nameVerifiedData,callback);
		    });
		}

//更新
		
		self.update = function(nameVerifiedData,callback){
			self.buttonControl = true;
			AjaxService.query('updateNameVerifiedData',nameVerifiedData)
			.then(function successCallback(response) {
				var data = response.data
				
				if(data['error']){
					alert(data['error']);
				}else{
					alert('更新完成');
				}
			}, function errorCallback(response) {
				console.log('Error result:');
				console.log(response);
				alert('error');
			}).finally(function(){
				self.buttonControl = false;
				self.query(nameVerifiedData,callback);
		    });
		}
//全域查詢
		
		self.conditions = [
			{name:"姓名",value:"name"},
			{name:"證號",value:"id"},
			{name:"發送認證時間(yyyy/MM/dd)",value:"date"},
			{name:"中華/香港號",value:"msisdn"},
			{name:"當地副號",value:"vln"},
			];
		

		self.searchHeader = [	
				{title:"證件類型",value:"type",size:"13em"},
				{title:"姓名",value:"name",size:"8em"},	           
	          	{title:"證號",value:"id",size:"8em"},
	          	{title:"中華/香港號",value:"msisdn",size:"8em"},	           
	          	{title:"當地副號",value:"vln",size:"8em"},
	          	{title:"發送時間",value:"sendDate",size:"10em"},
	          	{title:"狀態",value:"status",size:"3em"},
	          	];
		
		self.onSearchKeyDown = function(event){
			if(event.keyCode ==13){
				self.search();
			}
		}
		
		self.search = function(){
			self.nameVerifiedList = [];
			self.buttonControl = true;
			self.searching = true;
			var input = {
					input:self.searchValue,
					type:self.conditions[self.selectedCondition].value
			}
			AjaxService.query('searchNameVarifiedData',input)
			.then(function successCallback(response) {
				var data = response.data
				
				if(data['error']){
					alert(data['error']);
				}else{
					self.nameVerifiedList = data['data'];
				}
			}, function errorCallback(response) {
				console.log('Error result:');
				console.log(response);
				alert('error');
			}).finally(function(){
				self.buttonControl = false;
				self.searching = false;
		    });
		}
		
		
		
//分頁
		self.onePageNumber = 10;
		self.nowPage = 1;
		self.lastPage = 0;
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