package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만들어야 한다
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션 단위로 데이터베이스에 접근할때는 꼭 EntityManager를 생성해야 한다
        // EntityManager는 쓰레드간 공유가 되어서는 안된다 꼭 사용을 다하면 버려야 한다
        EntityManager em = emf.createEntityManager();
        // 이 영역에서 실제로 우리가 데이터베이스랑 소통한다
        // EntityManager 실행 단계에서는 꼭 EntityTransaction를 통해 트랜잭션을 생성해야 한다
        // 안그러면 데이터 조작 불가능(JPA의 모든 데이터 변경은 트랜잭션 안에서 실행)
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            // insert
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");
            em.persist(member);

            // select
            //Member findMember =  em.find(Member.class, 1L);
            //System.out.println("findMember.id = " + findMember.getId());
            //System.out.println("findMember.name = " + findMember.getName());

            // delete
            // em.remove(member);

            // update
            // 업데이트 할때는 commit이 필요 없다 트랜잭션 상에서 알아서 commit을 해준다
            // findMember.setName("HelloJPA");

            // JPQL 맛보기
            // 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
            // 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 SQL이 필요
            // 하지만 DB별로 방언이 존재하기에 엔티티 객체를 대상으로 쿼리를 조회할 수 있게 JPQL존재(DB 종속적이 약해짐)
            //List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    // 페이징 쿼리 역할
                     //.setFirstResult(1)
                     //.setMaxResults(10)
                     //.getResultList();

            // for (Member member : result) {
                // System.out.println("member = " + member.getName());
            // }

            // 비영속
            // Member member = new Member();
            //member.setId(100L);
            //member.setName("HelloJPA!!");

            // 영속
            System.out.println("=== BEFORE ===");
            // 여기서 1차 캐시로 저장
            em.persist(member);
            System.out.println("=== AFTER ===");

            // Member findMember = em.find(Member.class, 100L);

            // System.out.println(findMember.getId());
            // System.out.println(findMember.getName());

            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            // DB커넥션이 계속 유지 되기 때문에 한 트랜잭션이 끝나면 꼭 엔티티매니저를 닫아줘야 한다
            em.close();
        }
        emf.close();
    }
}