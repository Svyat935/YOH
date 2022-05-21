class StatRecord {
	constructor(params) {
		this.startTime = null;
		this.endTime = null;
		this.missClicks = [];
		this.answers = [];
		this.current_level = 1;
		this.current_level_time_start = null;
	}

	getTime() {
		return new Date().toLocaleString("en-US", {timeZone: "Europe/Moscow"});
	}

	enterLevel(params) {
		this.current_level = params['answer_number'];
		this.current_level_time_start = this.getTime();
	}

	gameStart(params) {
		this.startTime = this.getTime();
		this.enterLevel({
			answer_number: 1
		});
	}

	sendMissClick(params) {
		if (this.startTime) {
			if (params === null) {
				params = {};
			}
			params['answer_number'] = this.current_level;
			this.missClicks.push([this.getTime(), params]);
		}
	}

	sendAnswer(params) {
		if (params === null) {
			params = {};
		}
		params['time_start'] = this.current_level_time_start;
		params['answer_number'] = this.current_level;
		this.answers.push([this.getTime(), params]);
	}

	endGame(params) {
		this.endTime = this.getTime();
		let stats = {
			startTime: this.startTime,
			endTime: this.endTime,
			missClicks: this.missClicks,
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
