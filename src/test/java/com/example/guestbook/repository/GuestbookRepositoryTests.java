package com.example.guestbook.repository;

import com.example.guestbook.entity.Guestbook;
import com.example.guestbook.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {
    @Autowired
    private GuestbookRepository guestbookRepository;

    /*@Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .writer("user..."+(i%10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test
    public void updateTest(){
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if (result.isPresent()){
            Guestbook guestbook = result.get();
            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");

            guestbookRepository.save(guestbook);
        }
    }*/

    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(1, 10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        //가장 먼저 동적으로 처리하기 위해서 Q도메인 클래스를 얻어옵니다. Q도메인 클래스를 얻어옵니다. Q도메인 클래스를 이용하면 엔티티 클래스에 선언된 title,
        //content같은 필드들을 변수로 활용할 수 있습니다.

        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        //BooleanBuilder는 where 문에 들어가는 조건들을 넣어주는 컨테이너

        BooleanExpression expression = qGuestbook.title.contains(keyword);
        //원하는 조건은 필드값과 같이 결합해서 생성합니다. BooleanBuilder 안에 들어가는 값은 com.querydsl.core.type.Predicate타입이어야 합니다

        builder.and(expression);
        //만들어진 조건은 where문에 and 나 or 같은 키워드와 결합시킨다

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        //BooleanBuilder는 GuestbookRepository에 추가된 QuerydslPreducateExcutor인터페이스의 findAll()을 사용할 수 있습니다.

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
