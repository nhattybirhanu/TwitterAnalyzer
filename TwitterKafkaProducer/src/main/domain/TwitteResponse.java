package main.domain;

import java.util.List;


public class TwitteResponse {
		private List<Twitte> data;
		
		public Meta meta;
		

		public Meta getMeta() {
			return meta;
		}

		public void setMeta(Meta meta) {
			this.meta = meta;
		}

		public List<Twitte> getData() {
			return data;
		}

		public void setData(List<Twitte> data) {
			this.data = data;
		}

}
