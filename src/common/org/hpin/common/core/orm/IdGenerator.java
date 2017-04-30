package org.hpin.common.core.orm;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IncrementGenerator;

/**
 * 主键生成器
 * 
 * @author thinkpad
 * @Apr 25, 2010
 */
public class IdGenerator extends IncrementGenerator {

	@SuppressWarnings("unused")
	private static final AtomicInteger intSequence;

	static {
		intSequence = new AtomicInteger(0);
	}

	public synchronized Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		Long id = null;
		id = (Long) super.generate(session, object);
		return id;
	}
}
