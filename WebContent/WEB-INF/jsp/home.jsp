<jsp:include page="./templates/head.jsp" />



	<div class="row header-row  header-padding  ">
		  


		<!-- ALERTS -->  
  		<div class="alert alert-danger" id="errorMessageState" style="display:none" ng-style="errorMessageState">
		  <strong>Error!</strong> {{errorMessage}}
		</div>	
		
  		<div class="alert alert-success" id="successMessageState" style="display:none" ng-style="successMessageState">
		  <strong>Success!</strong> {{successMessage}}
		</div>	
		
		<div class="row hidden-xs hidden-sm" style="margin-top:1em"></div>
			

		
		
		<div class="row middle-row">
		  <center>
		 	 <div class="col-md-8 no-padding find-objects center-margin"> 
			
		
				<!-- FIND INPUT BOX -->
				<div class="open">
					<div class="alerttip alert alert-success text-left " id="inputTips" style="display:none" ng-style="inputTips">
					  <strong>Tip:</strong> add ? at the end to go to google search
					</div>	
		   			<div class="transparent input-group shadow find-group">
			   			
			   			<!-- **** FIND INPUT BOX ****-->
			   			<input type="text" style="width:79%" class="vcenter inline-display input-lg form-control dropdown-toggle large-input"  
			   				 id = "searchTag"
			   				 ng-model="searchTag" 
			   				 			 
			   				 placeholder="Enter text" 
			   				 autocomplete="off" 
			   				 value={{selectedprop.tag}}
			   				 ng-keyup="searchKeyUp($event)"
			   				 ng-keydown="searchKeyDown($event)"
			   				 ng-change="suggestTags()" autofocus are-expanded="true">
		   				 
		   				<!-- **** FIND BUTTON ****-->
	   				 	<button  style="width:20%; padding-left:1px; padding-right:1px;" class="pull-right vcenter inline-display btn btn-success btn-lg large-input"
	   				 		 
	   				 		 id="jumponBut" 
	   				 		 ng-click="getTag()" 
	   				 		 data-toggle="tooltip" 
	   				 		 data-placement="bottom" 
	   				 		 title="Jump on it!">
	   				 		 
	   				 	    <div class="hidden-xs hidden-sm">Jump</div>
	   				 		<div class="center-block vcenter">
	   				 		<span class="text-center glyphicon glyphicon-circle-arrow-right hidden-md hidden-lg" title="Jump"></span>
	   				 		</div>  
	   				 	</button>
		   				 
		    		</div>
		     		
		     		
		     		<!-- **** SUGGESTION LIST ****-->
					<div class="text-left list-group suggestList shadow " id="searchList">
					        <div class = "jump-item list-group-item " 
					            ng-repeat="prop in props" 
					        	ng-click="jump(prop)"
					        	ng-mousedown="handleClick($event, $index)" 
					        	ng-class="{ 'histJumps': prop.type == 'h', 'userJumps':prop.type == 'u', 'globalJumps':prop.type == 'g', 'searchJumps':prop.type == 's'}" 
					        	ng-cloak=""
					        	ng-style="a{{$index}}"
					        	id="a{{$index}}" 
					        	href="#" 
					        	data-toggle="tooltip" 
		   				 		data-placement="bottom" 
					        	title="Jump on it"
					        	ng-keyup="listKeyPress($event.keyCode, $index)">{{prop.tag}} 
					        		<!--<div class="pull-right">
						        		<a ng-click="search(prop)" data-toggle="tooltip" data-placement="bottom" title="Search in Google">
						        			<span class="glyphicon glyphicon-search"></span>
						        		</a> 
						        		 <a ng-click="search(prop)" title="Remove jump" style="padding-left:20px">
						        			<span class="glyphicon glyphicon-remove"></span>
						        		</a>  
					        		</div>-->					        		
					        </div>
					        
					</div>
				
					
					<!-- <div class="col-md-1 no-padding"> 							    
						
					</div> -->	
					
				</div>
			</div>	
		  </center>
		</div>
    
	  


		<div class="row bootom-row">
		  <div class="col-md-2"></div>
		  
		  <div class="col-md-2"></div>		  
		</div>


	 <!-- ADD ONS -->
	 <div class="text-center hidden-xs"> 
		  <div class="text-center">
			  <div class="text-center inline-display">
				  <button  class="btn btn-default btn-lg ext_buttons" ng-click="addSearchEngine()">
				  <span class="glyphicon glyphicon-zoom-in"></span>
				  Add as search engine
				  </button>
			  </div>		  
			  <div class="text-center inline-display">
				  <a class="btn btn-default btn-lg ext_buttons" href="https://chrome.google.com/webstore/detail/jumponio-quick-add/ajaadngokljakllpeigpkmboagpafaoa?utm_source=gmail">
				  <span class="glyphicon glyphicon-download"></span>
				  Install Chrome Extension
				  </a>
			  </div>
		  </div>
	</div>  




   <!-- CONTEXT MENU -->
   <div id="contextMenu" class="dropdown clearfix">
    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display:block;position:static;margin-bottom:5px;">
      <li><a  ng-click="rightClick(1)">Jump</a></li>	
      <li><a  ng-click="rightClick(2)">Search in Google</a></li>					      
      <li class="divider" ng-style="contextMenuVisibility" ></li>
      <li><a ng-style="contextMenuVisibility"  ng-click="rightClick(3)">Delete</a></li>
    </ul>
  </div>

 </div>


<jsp:include page="./templates/tail.jsp" />