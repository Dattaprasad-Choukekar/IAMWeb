select * from IDENTITIES
select * from IDENTITY_ATTRIBUTES

UPDATE ATTRIBUTES set ATTRIBUTE_NAME='Age', ATTRIBUTE_Type='TEXT' where ATTRIBUTE_ID = 1;
UPDATE ATTRIBUTES set ATTRIBUTE_NAME='City', ATTRIBUTE_Type='TEXT' where ATTRIBUTE_ID = 2;
commit;

select * from ATTRIBUTES;



Attribute atrb = new Attribute();
atrb.setName("Age");
 session = sessionFactory.openSession();
 org.hibernate.Transaction transaction = session.getTransaction();
transaction.begin();
	session.save(atrb);
	// int id = (int) session.save(identity);
	// identity.setId(id);
	transaction.commit();
	session.close();
	
	
	
	session = sessionFactory.openSession();
	list = session.createCriteria(Attribute.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
	session.close();
