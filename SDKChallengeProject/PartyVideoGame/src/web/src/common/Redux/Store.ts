import { createStore, combineReducers, applyMiddleware} from 'redux';
import { createLogger } from 'redux-logger';
import { StorageMiddleware } from 'common/Redux/Middlewares/StorageMiddleware';
import { sampleReducer } from 'common/Samples/SampleReducer';

const rootReducer = combineReducers({
  sample: sampleReducer,
});

export type RootState = ReturnType<typeof rootReducer>

const store = () => createStore(rootReducer, applyMiddleware(createLogger({ collapsed: true }), StorageMiddleware));

export default store;



