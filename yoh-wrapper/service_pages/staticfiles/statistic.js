class StatRecord {
	constructor(params) {
		this.startTime = null;
		this.endTime = null;
		this.answers = [];
		this.current_level = 1;
		this.current_level_time_start = null;
		this.count_clicks = 0;
		this.count_missclicks = 0;
		this.stat_func = function (stat_class) {
			stat_class.count_clicks++;
		};
	}

	getTime() {
		return new Date().toLocaleString("en-US", {timeZone: "Europe/Moscow"});
	}

	clearClicks() {
		this.count_clicks = 0;
		this.count_missclicks = 0;
	}

	enterLevel(params) {
		this.current_level = params['answer_number'];
		this.current_level_time_start = this.getTime();
		this.clearClicks();
	}

	gameStart(params) {
		this.startTime = this.getTime();
		this.enterLevel({
			answer_number: 1
		});
		document.addEventListener('click', this.stat_func(this));
	}

	sendMissClick(params) {
		if (this.startTime) {
			this.count_missclicks++;
			this.count_clicks--;
		}
	}

	sendAnswer(params) {
		if (params === null) {
			params = {};
		}
		params['time_start'] = this.current_level_time_start;
		params['answer_number'] = this.current_level;
		params['count_clicks'] = this.count_clicks;
		params['count_missclicks'] = this.count_missclicks;
		this.clearClicks();
		this.answers.push([this.getTime(), params]);
	}

	endGame(params) {
		this.endTime = this.getTime();
		let stats = {
			startTime: this.startTime,
			endTime: this.endTime,
			answers: this.answers
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
