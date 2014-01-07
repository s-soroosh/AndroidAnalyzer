package com.sarabadani.android.analyzer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AggregatedCalls extends ArrayList<AggregatedCall> {
	public Boolean has(String number) {
		for (Call c : this) {
			if (c.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	public AggregatedCall find(String number) {
		for (AggregatedCall c : this) {
			if (c.getNumber().equals(number)) {
				return c;
			}
		}
		return null;
	}

	public void aggregate(List<Call> calls) {
		for (Call c : calls) {
			if (this.has(c.getNumber())) {
				AggregatedCall aggregatedCall = this.find(c.getNumber());
				aggregatedCall.increaseDuration(c.getDuration());
				aggregatedCall.increaseNumberOfCalls();
			} else {
				this.add(new AggregatedCall(c));
			}
		}
		calculatePercentage();
		Collections.sort(this, new Comparator<AggregatedCall>() {

			@Override
			public int compare(AggregatedCall lhs, AggregatedCall rhs) {
				 if (lhs.getDuration() == rhs.getDuration()) return 0;
	                if (lhs.getDuration() < rhs.getDuration()) return 1;
	                else return -1;
			}
		});

	}

	private void calculatePercentage() {
		int sum = calculateTotalDuration();
		for (AggregatedCall call : this) {

			call.setPercentage((double) Math.round((((double) call.getDuration() / sum) * 100)));
		}
	}

	private int calculateTotalDuration() {
		int sum = 0;
		for (Call c : this) {
			sum += c.getDuration();
		}

		return sum;
	}
}