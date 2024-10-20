package TreeProject.repositories;

import TreeProject.models.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeRepository extends JpaRepository<Tree, Integer>  {
    List<Tree> findByPersonId(Integer personId);
}
