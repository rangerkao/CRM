angular.module('mService',[])
	.factory('AjaxService',['$http',function($http){
		var datas={data:'test'};
		return {
			//1.6版起，廢止success與error function，必須使用新的then function
			query:function(url,params){
				return $http({
					method:'POST',
					url:url,
					data:params,
				    cache: false,
					headers:{
						'Content-Type':'application/x-www-form-urlencoded',
						'Accept': 'application/json'},
					transformRequest:function(data,headers){
						var requestStr=null;
						for(var key in data){
							if(requestStr){
								requestStr += '&' + key + '=' +data[key];
							}else{
								requestStr = key + '=' + data[key];
							}
						}
						return requestStr;
					},
					transformResponse:function(data,headersGetter){
						//轉兩次才會成為Object
						
						try {
					        JSON.parse(data);
					    } catch (e) {
					        return data;
					    }
						var temp = angular.fromJson(data);
						return angular.fromJson(temp);
					}
				
				});  
			}
		};
		$httpProvider.defaults.headers.post['Content-Type']='application/x-www-form-urlencoded';
	}])
	.factory('ActionService',[function($http){
		return {
			block:function(){
				$.blockUI({ css: { 
		            border: 'none', 
		            padding: '15px', 
		            backgroundColor: '#000', 
		            '-webkit-border-radius': '10px', 
		            '-moz-border-radius': '10px', 
		            opacity: .5, 
		            color: '#fff' 
		        } });
			},
			unblock:function(){
				$.unblockUI();
			}
		};
	}]).service('DateFormatString', function(){
		this.Format = function (dayTime){
			var year=dayTime.getFullYear();
			var month=dayTime.getMonth()+1;
			var day=dayTime.getDate();
					
			if(month<10){
				month = '0'+month;
			}
					
			if(day<10){
				day = '0'+day;
			}
			
			return year+""+month+""+day;
		};
	});