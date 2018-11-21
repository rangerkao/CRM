<!-- Modal -->
<div class="modal fade" id="{{mid}}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document" style="max-width: 100%; {{width?'width:'+width:''}}">
    <div class="modal-content" style="background-image: url(pic/bg.png);">
      <div class="modal-header">
        <h5 class="modal-title">{{ title }}</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-show="showClose">
			<span aria-hidden="true">&times;</span>
		</button>
      </div>
      <div class="modal-body" ng-transclude>
        ...
      </div>
      <div class="modal-footer">
        <button ng-show="yes" type="button" class="btn btn-primary" data-dismiss="modal" ng-click="yesFunction()">OK</button>
		<button ng-show="no" type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
		