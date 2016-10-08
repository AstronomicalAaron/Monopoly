var $ui;
var uiWidth, uiHeight;

var $card;
var cardWidth, cardHeight;

var cards;
$.getJSON("tiles", function(json) {
	cards = json;
});

$(document).ready(function() {
	$ui = $('#ui');
	uiWidth = $ui.width();
	uiHeight = $ui.height();
	var uiOffset = $ui.offset();
	uiOffsetLeft = uiOffset.left;
	uiOffsetTop = uiOffset.top;
	
	$card = $('#card');
	cardWidth = $card.width();
	cardHeight = $card.height();
});

var app = angular.module('monopolyApp', []);
app.controller('monopolyController', function($scope) {
	
	$scope.cardHover = false;
	
	// Used a short name so the html isn't as bad
	$scope.card = function(c) {
		if ($scope.cardHover == true) {
			return;
		}
		
		$scope.selected = cards[c];
		
		if (c == null) {
			return;
		}
		
		// Configure card style
		$scope.cardHeaderStyle = {'background':$scope.selected.color};
		
		// Stuffs about to get gnar
		var td = $(window.event.target);
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
			cardX = Math.sign(cardX) * xLimit;
		}
		var yLimit = uiHeight / 2 + overlap - halfHeight;
		if (Math.abs(cardY) > yLimit) {
			cardY = Math.sign(cardY) * yLimit;
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
	
	$scope.isSelected = function() {
		return $scope.selected != null;
	}
	
	$scope.isProperty = function() {
		return $scope.selected != null && $scope.selected.type == 'Property';
	}
});