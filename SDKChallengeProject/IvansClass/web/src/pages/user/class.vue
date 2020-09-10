<template>
  <div>
    <user-menu></user-menu>

    <div class="course-container">
    <el-container>
      <el-header>我加入的班级</el-header>
      <el-main>
        <el-row type="flex" justify="space-around">
          <el-col :span="6" v-for="(item) in classList" v-bind:key="item.id">
            <div class="course-show">
              <div>
                <el-image
                  style="width: 100%; height: 200px"
                  :src="item.class.cover_image"
                  fit="cover">
                </el-image>
              </div>
              <p class="course-name">
                {{item.class.course.name}}-{{item.class.class_name}}
              </p>
              <p class="class-desc">
                {{item.class.description}}
              </p>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
  </div>
</template>

<script>

import { fetchUserClasses } from '../../utils/api'
import UserMenu from '../../components/UserMenu'

export default {
  name: 'UserClass',
  components: { UserMenu },
  mounted () {
    this.fetchUserClasses()
  },
  data () {
    return {
      course_id: 0,
      param: {
        page: 1,
        rows: 10
      },
      classList: [],
      courseInfo: {}
    }
  },
  methods: {
    fetchUserClasses () {
      const that = this
      const param = {}
      fetchUserClasses(param).then((res) => {
        that.classList = res.data.data
      })
    }
  }
}
</script>

<style>
  .course-container {
    margin: 30px auto;
  }

  .course-name {
    font-weight: bold;
  }

  .course-show {
    border: 1px solid #eee;
    padding: 15px;
    box-shadow: 0 0 5px #909399;
  }

  .course-show:hover {
    box-shadow: 0 0 25px #909399;

  }
</style>
