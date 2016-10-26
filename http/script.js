var $ui;
var uiWidth, uiHeight;

var $card;
var cardWidth, cardHeight;

var $menu;
var $menuToggle;

var $join;
var $joinBackground;
var $joinName;
var $joinToken;

$(document).ready(function() {
	
	// ui
	$ui = $('#ui');
	uiWidth = $ui.width();
	uiHeight = $ui.height();
	var uiOffset = $ui.offset();
	uiOffsetLeft = uiOffset.left;
	uiOffsetTop = uiOffset.top;
	
	// card
	$card = $('#hover-card');
	cardWidth = $card.width();
	cardHeight = $card.height();
	
	// menu
	$menu = $('#menu');
	$menuToggle = $('#menu-toggle');
	
	// join
	$join = $('#join');
	$joinBackground = $('#join-background');
	$joinName = $('#join-name');
	$joinToken = $('#join-token');
});

function poll($scope) {
	$.getJSON('state', function(json) {
		update($scope, json);
		
		setTimeout(function() {
			poll($scope)
		}, 1000);
	});
}

function update($scope, json) {
	$scope.state = json;
	$scope.$apply();
	
	if ($scope.state.phase == "WAITING") {
		return;
	}
	
	$scope.currentPlayer = $scope.state.players[$scope.state.currentPlayerIndex];
	$scope.currentTile = $scope.state.board.tiles[$scope.currentPlayer.token.tileIndex];
}

var app = angular.module('monopolyApp', []);
app.controller('monopolyController', function($scope) {
	
	$scope.cardHover = false;
	$scope.cardHeaderStyle = {'background':'white','color':'black'};
	
	poll($scope);
	
	$scope.getOp = function(path, callback) {
		$.getJSON(path, function(json) {
			update($scope, json)
			
			if (callback != undefined) {
				callback(true);
			}
		})
		.fail(function(json) {
			if (callback != undefined) {
				callback(false);
			}
		});
	}
	
	$scope.postOp = function(path, data, callback) {
		$.post(path, data, function(json) {
			update($scope, json)
			
			if (callback != undefined) {
				callback(true);
			}
		}, 'json')
		.fail(function() {
			if (callback != undefined) {
				callback(false);
			}
		});
	}
	
	$scope.toggleMenu = function() {
		
		if ($menu.is(':visible')) {
			$menu.animate({left:'-300px'}, 100, 'swing', function() {
				// hide after animation is complete
				$menu.hide();
			});
			$menuToggle.animateRotate(90, 0, 100);
		}
		else {
			$menu.animate({left:'0px'}, 100, 'swing');
			$menu.show();
			$menuToggle.animateRotate(0, 90, 100);
		}
	}
	
	// Used a short name so the html isn't as bad
	$scope.card = function($event) {
		if ($scope.cardHover == true) {
			return;
		}
		
		var isTile = $.isNumeric($event.target.id) ^ ($event.type == 'mouseleave');
		if (isTile) {
			var c = $event.target.id;
			$scope.selected = $scope.state.board.tiles[c];
		}
		else {
			$scope.selected = null;
			return;
		}
		
		if ($scope.selected == null) return;
			
		// Stuffs about to get gnar
		var td = $($event.target);
		// Get td position
		var offset = td.offset();
		var eventX = offset.left + td.width() / 2;
		var eventY = offset.top + td.height() / 2;
		// Get center
		var uiOffset = $ui.offset();
		var centerX = uiOffset.left + uiWidth / 2;
		var centerY = uiOffset.top + uiHeight / 2;
		// Clamp card position to center square plus a small overlap
		// To start, coordinates are relative to the center
		var overlap = 15;
		var cardX = eventX - centerX;
		var cardY = eventY - centerY;
		var halfWidth = $card.width() / 2;
		var halfHeight = $card.height() / 2;
		// clamp
		var xLimit = uiWidth / 2 + overlap - halfWidth;
		if (Math.abs(cardX) > xLimit) {
			cardX = (cardX / Math.abs(cardX)) * xLimit;
		}
		var yLimit = uiHeight / 2 + overlap - halfHeight;
		if (Math.abs(cardY) > yLimit) {
			cardY = (cardY / Math.abs(cardY)) * yLimit;
		}
		// position relative to upper left corner
		cardX += centerX;
		cardY += centerY;
		// center card
		cardX -= cardWidth / 2;
		cardY -= cardHeight / 2;
		// Apply position to card
		$card.css({left: cardX, top: cardY});
	}
	
	$scope.spectate = function() {
		$join.hide();
		$joinBackground.hide();
	}
	
	$scope.chooseToken = function() {
		$joinName.hide();
		$joinToken.show();
	}
	
	$scope.join = function(token) {
		$scope.getOp('join?name=' + $scope.username + "&token=" + token,
		function(success) {
			if (success) {
				$join.hide();
				$joinBackground.hide();
			}
			else {
				$joinToken.hide();
				$joinName.show();
			}
		});
	}
	
	$scope.start = function () {
		$scope.getOp('start');
	}
	
	//Buy property
	$scope.buyProperty = function(){
		$scope.getOp('buyproperty');
	}
	//
	
	//Sell property
	$scope.sellProperty = function(property, recipient, amount){
		$scope.getOp('sellproperty?property='
				+ property + '&recipient=' 
				+ recipient + '&amount='
				+ amount );
	}
	//
	
	//Auction - not made yet
	$scope.auction = function(){
		
	}
	//
	
	//Buy Mortage
	$scope.buyMortgage = function(){
		$scope.getOp('buymortgage');
	}
	//
	
	//Lift Mortgage
	$scope.liftMortgage = function(){
		$scope.getOp('liftmortgage');
	}
	//
	
	//Upgrade/improve property
	$scope.upgradeProp = function(){
		$scope.getOp('upgradeprop');
	}
	//
	
	//Degrade property
	$scope.degradeProp = function(){
		$scope.getOp('degradeprop');
	}
	//
	
	//Go to jail
	$scope.goToJail = function(){
		
	}
	//
	
	$scope.payRent = function(){
		$scope.getOp('payrent');
	}
	
	var rollAnimationStarted = false;
	
	$scope.phaseRolling = function() {
		if ($scope.state != null && $scope.state.phase == "ROLLING") {
			// Do the roll animation if it has not already started
			if (rollAnimationStarted == false) {
				rollAnimationStarted = true;
				rolldice($scope);
			}
			return true;
		}
		else {
			rollAnimationStarted = false;
			return false;
		}
	}
	
	$scope.cardStyle = function (tile) {
		if (tile == null) {
			return;
		}
		
		var style = {};
		
		// Configure card style
		var color = tile.color;
		style.background = color;

		// invert the dark blue ones
		style.color = color == '#0072bb' ? 'white' : 'black';
		
		if (tile.type == 'UTILITY'){
			var imageLocation;
			if (tile.name == "WATER WORKS") {
				imageLocation = "waterworks.png";
			} else {
				imageLocation = "electricompany.png";
			}
			$('#utilityImage').src=imageLocation;
		}
		
		return style;
	}
	
	$scope.isSelected = function() {
		return $scope.selected != null;
	}
	$scope.isProperty = function(tile) {
		return tile != null && tile.type == 'PROPERTY';
	}
	$scope.isUtility = function(tile) {
		return tile != null && tile.type == 'UTILITY';
	}
	$scope.isTaxes = function(tile) {
		return tile != null && tile.type == 'TAXES';
	}
	$scope.isRailroad = function(tile) {
		return tile != null && tile.type == 'RAILROAD';
	}
	$scope.isChance = function(tile) {
		return tile != null && tile.type == 'CHANCE';
	}
	$scope.isCommunityChest = function(tile) {
		return tile != null && tile.type == 'COMMUNITYCHEST';
	}
	$scope.isGo = function(tile) {
		return tile != null && tile.type == 'GO';
	}
	$scope.isJail = function(tile) {
		return tile != null && tile.type == 'JAIL';
	}
	$scope.isFreeParking = function(tile) {
		return tile != null && tile.type == 'FREEPARKING';
	}
	$scope.isGoToJail = function(tile) {
		return tile != null && tile.type == 'GOTOJAIL';
	}
	
	$scope.tokenStyle = function(player) {
		// Get the tile the token is on
		var tile = $("#" + player.token.tileIndex);
		// Get copy the position to the token
		var position = tile.position();
		
		// Put the token in the center of the tile
		var marginLeft = tile.width() / 2 - 20;
		var marginTop = tile.height() / 2 - 20;
		
		return {
			'background-image': 'url("' + player.token.type.toLowerCase() + '.png")',
			left: position.left,
			top: position.top,
			'margin-left': marginLeft,
			'margin-top': marginTop
		};
	}
});

$.fn.animateRotate = function(start, end, duration, easing, complete) {
  var args = $.speed(duration, easing, complete);
  var step = args.step;
  return this.each(function(i, e) {
    args.complete = $.proxy(args.complete, e);
    args.step = function(now) {
      $.style(e, 'transform', 'rotate(' + now + 'deg)');
      if (step) return step.apply(e, arguments);
    };

    $({deg: start}).animate({deg: end}, args);
  });
};

var counter = 0;

function rolldice($scope) {
	counter = 0;
	rolldiceaux($scope);
}

function rolldiceaux($scope) {
	var die1 = document.getElementById('dice-one');
	var die2 = document.getElementById('dice-two');
	
	var backgrounds = [
	"url('dice1.png')",
	"url('dice2.png')",
	"url('dice3.png')",
	"url('dice4.png')",
	"url('dice5.png')",
	"url('dice6.png')"];
	
	// There might be a case where the client lags, and the rolling phase
	// ends before the client-side animation does. Check for this.
	if (counter > 10 || $scope.state.phase != "ROLLING"){
		die1.style.backgroundImage = backgrounds[$scope.state.board.dice[0].value - 1];
		die2.style.backgroundImage = backgrounds[$scope.state.board.dice[1].value - 1];
		return;
	}		
	else{
		die1.style.backgroundImage = backgrounds[Math.floor((Math.random() * 6) + 1)];
		die2.style.backgroundImage = backgrounds[Math.floor((Math.random() * 6) + 1)];
	}
	
	counter++;
	
	setTimeout(function(){
		rolldiceaux($scope);
	}, 100);
}