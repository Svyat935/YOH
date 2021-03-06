class StatRecord {
	constructor(params) {
		// 	params = {
		// 		'levels_names': ['1 уровень', '2 уровень'...]
		// 	}
		this.levels_names = params['levels_names'];
		this.current_level = null;
		this.current_level_time_start = null;
		this.count_clicks = 0;
		this.count_missclicks = 0;
		this.stat_func = function (stat_class) {
			return function (event) {
				if (Number.isInteger(stat_class.current_level)) {
					stat_class.count_clicks++;
				}
			}
		};
		this.additional_fields = null;
		this.getAddFields();
		window.addEventListener('click', this.stat_func(this));
	}

	async getAddFields() {
		let response = await fetch('/api/additional_fields').then((resp) => {
			return resp.json();
		});
		this.additional_fields = response;
	}

	getTime() {
		return new Date().toLocaleString("en-US", {timeZone: "Europe/Moscow"});
	}

	clearClicks() {
		this.count_clicks = 0;
		this.count_missclicks = 0;
	}

	enterLevel(params) {
		// params = {
		// 		'answer_number': 0  # Номер вопроса = номер индекса levels_names
		// }
		if (this.current_level !== null) {
			let params_to_send = {
				level: this.current_level,
				level_name: this.levels_names[this.current_level],
				date_start: this.current_level_time_start,
				date_end: this.getTime(),
				type: 3,
				clicks: this.count_clicks,
				missclicks: this.count_missclicks,
				additional_fields: this.additional_fields,
				details: {}
			};
			fetch('/api/statistics', {method: 'POST', body: JSON.stringify(params_to_send)});
		}
		else {
			this.sendAddFields();
		}
		this.current_level = params['answer_number'];
		this.current_level_time_start = this.getTime();
		this.clearClicks();
	}

	sendLeaveGame() {
		if ('endGame' in window) {
			endGame.postMessage('it is end game');
		}
		parent.postMessage("it is end game", "*");
	}

	gameStart(params) {
		if (!params) {
			params = {};
		}
		params['date_start'] = this.getTime();
		params['details'] = this.additional_fields;
		fetch('/api/game_start', {method: 'POST', body: JSON.stringify(params)});
	}

	gameEnd(params) {
		if (!params) {
			params = {};
		}
		params['date_end'] = this.getTime();
		params['details'] = this.additional_fields;
		fetch('/api/game_end', {method: 'POST', body: JSON.stringify(params)});
	}

	sendAddFields(params) {
		if (!params) {
			params = {};
		}
		params['details'] = this.additional_fields;
		fetch('/api/additional_fields', {method: 'POST', body: JSON.stringify(params)});
	}

	sendMissClick(params) {
		if (Number.isInteger(this.current_level)) {
			this.count_missclicks++;
			this.count_clicks--;
		}
	}

	sendAnswer(params) {
		// params = {
		// 		'correct': True
		// }
		let params_to_send = {
			level: this.current_level,
			level_name: this.levels_names[this.current_level],
			date_start: this.current_level_time_start,
			date_end: this.getTime(),
			type: params['correct'] ? 1 : 2,
			clicks: this.count_clicks,
			missclicks: this.count_missclicks,
			additional_fields: this.additional_fields,
			details: {}
		};
		fetch('/api/statistics', {method: 'POST', body: JSON.stringify(params_to_send)});
		this.current_level = null;
		this.clearClicks();

	}
}
