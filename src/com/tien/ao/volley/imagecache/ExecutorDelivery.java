package com.tien.ao.volley.imagecache;

import java.util.concurrent.Executor;

import android.os.Handler;

import com.tien.ao.volley.VolleyError;

public class ExecutorDelivery implements ResponseDelivery {

	private final Executor mResponsePoster;

	public ExecutorDelivery(final Handler handler) {
		mResponsePoster = new Executor() {

			@Override
			public void execute(Runnable command) {
				handler.post(command);
			}
		};
	}

	public ExecutorDelivery(Executor executor) {
		mResponsePoster = executor;
	}

	@Override
	public void postResponse(Request<?> request, Response<?> response) {
		postResponse(request, response, null);
	}

	@Override
	public void postResponse(Request<?> request, Response<?> response,
			Runnable runnable) {
		mResponsePoster.execute(new ResponseDeliveryRunnable(request, response,
				runnable));
	}

	@Override
	public void postError(Request<?> request, VolleyError error) {

	}

	@SuppressWarnings("rawtypes")
	private class ResponseDeliveryRunnable implements Runnable {
		private final Request mRequest;
		private final Response mResponse;
		private final Runnable mRunnable;

		public ResponseDeliveryRunnable(Request request, Response response,
				Runnable runnable) {
			mRequest = request;
			mResponse = response;
			mRunnable = runnable;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			mRequest.deliverResponse(mResponse.result);

			// If we have been provided a post-delivery runnable, run it.
			if (mRunnable != null) {
				mRunnable.run();
			}
		}
	}

}
