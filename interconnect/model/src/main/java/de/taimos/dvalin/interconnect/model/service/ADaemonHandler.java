package de.taimos.dvalin.interconnect.model.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.taimos.dvalin.interconnect.model.InterconnectContext;
import de.taimos.dvalin.interconnect.model.ivo.IVO;
import de.taimos.dvalin.interconnect.model.ivo.daemon.PingIVO;
import de.taimos.dvalin.interconnect.model.ivo.daemon.PongIVO;

/**
 * Handles requests and receives of daemons.<br>
 * For every request/receive a new DaemonHandler is created so they MUST be stateless!
 *
 * @see Daemon
 */
public abstract class ADaemonHandler implements IDaemonHandler {

	/** Logger. */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public IContext getContext() {
		return InterconnectContext.getContext();
	}

	@Override
	public void exceptionHook(final RuntimeException e) throws DaemonError {
		// override if you like
	}

	@Override
	public void beforeRequestHook() {
		// override if you like
	}

	@Override
	public void afterRequestHook() {
		// override if you like
	}

	@Override
	public PongIVO alive(PingIVO req) {
		return new PongIVO.PongIVOBuilder().build();
	}


	public static final class Context implements IContext {

		private final Class<? extends IVO> requestClass;
		private final UUID uuid;
		private final int deliveryCount;
		private final boolean redelivered;


		/**
		 * @param aRequestClass Request class
		 * @param anUUID Universally unique identifier of the request
		 * @param aDeliveryCount Number of attempts to send the message (start at 1)
		 * @param aRedelivered True if the message is being resent to the daemon
		 */
		public Context(final Class<? extends IVO> aRequestClass, final UUID anUUID, final int aDeliveryCount, final boolean aRedelivered) {
			this.requestClass = aRequestClass;
			this.uuid = anUUID;
			this.deliveryCount = aDeliveryCount;
			this.redelivered = aRedelivered;
		}

		/**
		 * @param aRequestClass Request class
		 * @param anUUID Universally unique identifier of the request
		 */
		@Deprecated
		public Context(final Class<? extends IVO> aRequestClass, final UUID anUUID) {
			this(aRequestClass, anUUID, 0, false);
		}

		@Override
		public Class<? extends IVO> requestClass() {
			return this.requestClass;
		}

		@Override
		public UUID uuid() {
			return this.uuid;
		}

		@Override
		public int deliveryCount() {
			return this.deliveryCount;
		}

		@Override
		public boolean redelivered() {
			return this.redelivered;
		}

		@Override
		public String toString() {
			return "Context [uuid=" + this.uuid + "; deliveryCount=" + this.deliveryCount + "; redelivered=" + this.redelivered + "]";
		}
	}

}
