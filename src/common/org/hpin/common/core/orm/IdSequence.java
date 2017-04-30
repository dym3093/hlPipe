package org.hpin.common.core.orm;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.SequenceGenerator;

/**
 * 扩张Hibernate主键生成器
 * 
 * @author thinkpad
 * 
 */
public class IdSequence extends SequenceGenerator {

	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		Long seq = null;
		seq = (Long) super.generate(session, object);
		return seq;
	}
}
