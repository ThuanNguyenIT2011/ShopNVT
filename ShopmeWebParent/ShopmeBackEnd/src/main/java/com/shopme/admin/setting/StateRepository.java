
package com.shopme.admin.setting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

}
