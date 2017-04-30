package org.hpin.base.dict.service;

import org.hpin.common.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Title:id2name
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * </p>
 * 
 * @author sherry
 * @version 0.1
 * 
 */

@Service(value = "org.hpin.base.dict.service.ID2NameIService")
@Transactional()
public interface ID2NameIService{
	/**
	 * id转name
	 * 
	 * @param id
	 *            一般为表中的主键
	 * @param beanId
	 *            对应dao(表)的beanId
	 * @return 返回主键对应的name(自定义)
	 * @throws BusinessException
	 * @since 0.1
	 */
	public String id2Name(String id, String beanId);
}
