class StatRecord {
	constructor(params) {
		this.startTime = null;
		this.endTime = null;
		this.missClicks = [];
		this.answers = [];
		this.clicks_info = {};
		this.stat_func = function (stat_class) {
			return function (event) {
				let x = event.layerX - event.layerX % 10;
				let y = event.layerY - event.layerY % 10;
				let x_y = x + '.' + y;

				if (x_y in stat_class.clicks_info) {
					stat_class.clicks_info[x_y]++;
				}
				else {
					stat_class.clicks_info[x_y] = 1;
				}
			};
		};
	}

	gameStart(params) {
		this.startTime = new Date().toLocaleString("en-US", {timeZone: "Europe/Moscow"});
		window.addEventListener('click', this.stat_func(this));
	}

	sendMissClick(params) {
		if (this.startTime) {
			this.missClicks.push([new Date().toLocaleString("en-US", {timeZone: "Europe/Moscow"}), params]);
		}
	}

	sendAnswer(params) {
		this.answers.push([new Date().toLocaleString("en-US", {timeZone: "Europe/Moscow"}), params]);
	}

	endGame(params) {
		this.endTime = new Date().toLocaleString("en-US", {timeZone: "Europe/Moscow"});
		let stats = {
			startTime: this.startTime,
			endTime: this.endTime,
			missClicks: this.missClicks,
			answers: this.answers,
			clicks: this.clicks_info,
			window_info: {
				width: window.innerWidth,
				height: window.innerHeight
			}
		};
		fetch('/api/send_statistic', {method: 'POST', body: JSON.stringify(stats)});
		this.#sendEndGameForMobile();
	}

	#sendEndGameForMobile() {
		if ('endGame' in window) {
			endGame.postMessage('it is end game');
		}
	}

}
