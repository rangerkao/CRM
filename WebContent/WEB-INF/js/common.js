angular.module('MainApp',['mService'])
	.controller('MainCtrl',[function(){
		var self=this;
		
	}])
	.directive('modal', [function () {
	    return {
	    	templateUrl:'./directive/modal',
	        //restrict: Attributes,Element,Class,Mark
	    	restrict: 'E',
	    	//替換ng-transclude屬性的內容
	    	//true 一般內容替換，directive template 會包覆 element 內容。
	    	//'element' 元素替換，directive template 會包覆 element
	        transclude: true,
	        //replace:true,
	        //Scope: false(預設)與父相同、true繼承並新建、object傳遞物件並建立獨立範疇
	        scope:{
	        	// = 指定屬性的值為JSON物件，父範疇的變更會自動套用
	        	// @ 指定屬性值為字串
	        	// & 指定屬性值為某個controller的函式，可在需要時觸發
	        	
	        	mid:'@',
	        	title:'@',
	        	width:'@',
	        	showYes:'@',
	        	showNo:'@',
	        	showClose:'@',
	        	yesFunction:'&',
	        },
	        link: function postLink(scope, element, attrs) {
	        	
	        	if(scope.showYes=='false')
	        		scope.yes=false;
	        	else
	        		scope.yes=true;
	        	
	        	if(scope.showNo=='false')
	        		scope.no=false;
	        	else
	        		scope.no=true;
	        	
	        	//取用元素
	        	//當 param=B ，scope.param
	        	//當 directive=B ,attrs.directive

	        }
	    };
	}])
	;
